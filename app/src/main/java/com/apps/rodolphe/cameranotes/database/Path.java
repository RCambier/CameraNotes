package com.apps.rodolphe.cameranotes.database;

public class Path {
    private long id;
    private String path;
    private String shape = null;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return path + ", "+ shape;
    }
} 