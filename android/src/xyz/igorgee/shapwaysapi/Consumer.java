package xyz.igorgee.shapwaysapi;

public enum Consumer {
    KEY("YOUR-KEY-HERE"),
    SECRET("YOUR-SECRET-HERE");

    private String value;

    Consumer(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
