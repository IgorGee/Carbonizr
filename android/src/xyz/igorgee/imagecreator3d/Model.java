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

    public void setLocation(File location) {
        this.location = location;
    }

    public Integer getModelID() {
        return modelID;
    }

    public void setModelID(Integer modelID) {
        this.modelID = modelID;
    }
}
