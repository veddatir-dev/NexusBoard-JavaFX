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
    private final int PORT = 5000;
    private @SuppressWarnings("unused") NetworkCallback callback;

    public static NetworkManager getInstance() { return instance; }

    public void setCallback(NetworkCallback callback) {
        this.callback = callback;
    }

    public void startServer(NetworkCallback callback) {
        this.callback = callback;
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server Started. Waiting for peer…");
                socket = serverSocket.accept();
                setupStreams(callback);
            } catch (IOException e) { e.printStackTrace(); }
        }).start();
    }

    public void connectToPeer(String ip, NetworkCallback callback) {
        this.callback = callback;
        new Thread(() -> {
            try {
                socket = new Socket(ip, PORT);
                setupStreams(callback);
            } catch (IOException e) { e.printStackTrace(); }
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
                    @SuppressWarnings("unchecked")
					Map<String, Double> props = (Map<String, Double>) in.readObject();
                    Platform.runLater(() -> {
                        if (type.equals("CLEAR")) {
                            callback.clearAllShapes();
                        } else if (type.equals("UNDO")) {
                            callback.undoLastShape();
                        } else {
                            iShape shape = ShapeFactory.getShape(type);
                            if (shape != null) {
                                shape.setProperties(props);
                                callback.addExternalShape(shape);
                            }
                        }
                    });
                }
            } catch (Exception e) { 
                System.out.println("Peer disconnected."); 
                Platform.runLater(() -> callback.updateConnectionStatus("Peer disconnected"));
            }
        }).start();
    }

    public void broadcastShape(iShape shape) {
        if (out == null || shape == null) return;
        new Thread(() -> {
            try {
                out.writeObject(shape.getClass().getSimpleName());
                out.writeObject(shape.getProperties());
                out.flush();
            } catch (IOException e) { e.printStackTrace(); }
        }).start();
    }

    public void broadcastCommand(String command) {
        if (out == null) return;
        new Thread(() -> {
            try {
                out.writeObject(command);
                out.writeObject(new java.util.HashMap<String, Double>()); // Empty map
                out.flush();
            } catch (IOException e) { e.printStackTrace(); }
        }).start();
    }
}
