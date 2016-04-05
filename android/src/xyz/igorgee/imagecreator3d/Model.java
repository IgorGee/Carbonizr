package xyz.igorgee.imagecreator3d;

import java.io.File;

public class Model {
    String name;
    File location; // Folder that contains the g3db and stl files.
    Integer modelID;

    public Model(String name, File location) {
        this.name = name;
        this.location = location;
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

    public File getImageLocation() {
        return new File(location, name + ".jpg");
    }

    public File getStlLocation() {
        return new File(location, name + ".stl");
    }

    public File getG3dbLocation() {
        return new File(location, name + ".g3db");
    }

    public Integer getModelID() {
        return modelID;
    }

    public void setModelID(Integer modelID) {
        this.modelID = modelID;
    }
}
