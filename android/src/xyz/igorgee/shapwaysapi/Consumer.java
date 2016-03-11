package xyz.igorgee.shapwaysapi;

public enum Consumer {
    KEY("b0231f7907e3e7d7a6cb016a92d2d8b7d71fa252"),
    SECRET("fd704d47663a5c40b38c5a326ad3c7b3f55f42da");

    private String value;

    Consumer(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
