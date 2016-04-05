package xyz.igorgee.imagecreator3d;

public enum ModelStatus {
    SUCCESS("yes"),
    FAILED("no"),
    PENDING("processing"),
    UNKNOWN("unkown");

    String status;

    ModelStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static ModelStatus getModelStatus(String status) {
        for (ModelStatus modelStatus : ModelStatus.values()) {
            if (modelStatus.getStatus().equals(status)) {
                return modelStatus;
            }
        }

        throw new IllegalArgumentException("No such status: " + status + " exists");
    }
}
