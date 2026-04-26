package com.nexus.network;

import com.nexus.model.iShape;
import com.nexus.model.ShapeFactory;
import javafx.application.Platform;
import java.io.*;
import java.net.*;
import java.util.Map;

public class NetworkManager {
    private static NetworkManager instance = new NetworkManager();
    private Socket socket;
    private ObjectOutputStream out;
    private int PORT = 5000;
    private @SuppressWarnings("unused") NetworkCallback callback;
    public boolean isRemoteChange = false;

    public static NetworkManager getInstance() { return instance; }

    public void setCallback(NetworkCallback callback) {
        this.callback = callback;
    }

    public void startServer(int port, NetworkCallback callback) {
        this.PORT = port;
        this.callback = callback;
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server Started. Waiting for peer…");
                socket = serverSocket.accept();
                setupStreams(callback);
            } catch (IOException e) { 
                e.printStackTrace(); 
                if (callback != null) {
                    Platform.runLater(() -> callback.updateConnectionStatus("Server Error: " + e.getMessage()));
                }
            }
        }).start();
    }

    public void connectToPeer(String ip, int port, NetworkCallback callback) {
        this.PORT = port;
        this.callback = callback;
        new Thread(() -> {
            try {
                socket = new Socket(ip, PORT);
                setupStreams(callback);
            } catch (IOException e) { 
                e.printStackTrace(); 
                if (callback != null) {
                    Platform.runLater(() -> callback.updateConnectionStatus("Connection Error: " + e.getMessage()));
                }
            }
        }).start();
    }

    private void setupStreams(NetworkCallback callback) throws IOException {
        this.callback = callback;
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connection established!");
        Platform.runLater(() -> callback.updateConnectionStatus("Connected to peer"));

        new Thread(() -> {
            try {
                while (true) {
                    String type = (String) in.readObject();
                    Object payload = in.readObject();
                    String shapeText = null;
                    if (!type.equals("CLEAR") && !type.equals("UNDO") && !type.equals("CONNECTION_EVENT") && !type.startsWith("TEXT_")) {
                        shapeText = (String) in.readObject();
                    }
                    
                    final String finalText = shapeText;
                    Platform.runLater(() -> {
                        isRemoteChange = true;
                        if (type.equals("CLEAR")) {
                            callback.clearAllShapes();
                        } else if (type.equals("UNDO")) {
                            callback.undoLastShape();
                        } else if (type.startsWith("TEXT_")) {
                            callback.updateText(type, (String) payload);
                        } else {
                            if (payload instanceof Map) {
                                @SuppressWarnings("unchecked")
                                Map<String, Double> props = (Map<String, Double>) payload;
                                iShape shape = ShapeFactory.getShape(type);
                                if (shape != null) {
                                    shape.setProperties(props);
                                    if (finalText != null) {
                                        shape.setText(finalText);
                                    }
                                    callback.addExternalShape(shape);
                                }
                            }
                        }
                        isRemoteChange = false;
                    });
                }
            } catch (Exception e) { 
                System.out.println("Peer disconnected."); 
                Platform.runLater(() -> callback.updateConnectionStatus("Peer disconnected"));
            }
        }).start();
    }

    public void broadcastShape(iShape shape) {
        if (out == null || shape == null || isRemoteChange) return;
        new Thread(() -> {
            try {
                out.writeObject(shape.getClass().getSimpleName());
                out.writeObject(shape.getProperties());
                out.writeObject(shape.getText() != null ? shape.getText() : "");
                out.flush();
            } catch (IOException e) { e.printStackTrace(); }
        }).start();
    }

    public void broadcastCommand(String command) {
        if (out == null || isRemoteChange) return;
        new Thread(() -> {
            try {
                out.writeObject(command);
                out.writeObject(new java.util.HashMap<String, Double>()); // Empty map
                out.flush();
            } catch (IOException e) { e.printStackTrace(); }
        }).start();
    }
    
    public void broadcastConnectionEvent(String sourceID, String targetID, String lineType) {
        if (out == null || isRemoteChange) return;
        
        String jsonStr = "{\"type\":\"CONNECTION_EVENT\",\"sourceID\":\"" + sourceID + 
                         "\",\"targetID\":\"" + targetID + "\",\"lineType\":\"" + lineType + "\"}";
        
        new Thread(() -> {
            try {
                out.writeObject("CONNECTION_EVENT");
                out.writeObject(jsonStr);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void broadcastText(String type, String text) {
        if (out == null || isRemoteChange) return;
        new Thread(() -> {
            try {
                out.writeObject(type);
                out.writeObject(text);
                out.flush();
            } catch (IOException e) { e.printStackTrace(); }
        }).start();
    }
}
