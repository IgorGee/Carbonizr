package xyz.igorgee.imagecreator3d;

import java.io.File;

public class Model {
    String name;
    File location; // Folder that contains the g3db and stl files.
    File imageLocation;
    File stlLocation;
    File g3dbLocation;
    Integer modelID;

    public Model(String name, File location) {
        this.name = name;
        this.location = location;
        this.imageLocation = new File(location, name + ".jpg");
        this.stlLocation = new File(location, name + ".stl");
        this.g3dbLocation = new File(location, name + ".g3db");
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

    public File getImageLocation() {
        return imageLocation;
    }

    public File getStlLocation() {
        return stlLocation;
    }

    public File getG3dbLocation() {
        return g3dbLocation;
    }

    public Integer getModelID() {
        return modelID;
    }

    public void setModelID(Integer modelID) {
        this.modelID = modelID;
    }
}
