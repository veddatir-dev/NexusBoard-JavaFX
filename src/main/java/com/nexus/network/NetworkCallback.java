package com.nexus.network;

import com.nexus.model.iShape;

public interface NetworkCallback {
    void updateConnectionStatus(String status);
    void addExternalShape(iShape shape);
    void clearAllShapes();
    void undoLastShape();
    void updateText(String type, String text);
}