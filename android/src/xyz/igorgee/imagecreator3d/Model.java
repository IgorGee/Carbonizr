package xyz.igorgee.imagecreator3d;

import android.graphics.Bitmap;

import java.io.File;

public class Model {
    String name;
    File location; // Folder that contains the g3db and stl files.
    Integer modelID;
    Bitmap bitmap;

    public Model(String name, File location, Bitmap bitmap) {
        this.name = name;
        this.location = location;
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getLocation() {
        return location;
    }

    public void setLocation(File location) {
        this.location = location;
    }

    public Integer getModelID() {
        return modelID;
    }

    public void setModelID(Integer modelID) {
        this.modelID = modelID;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
