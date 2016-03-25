package xyz.igorgee.shapwaysapi;

public enum Discovery {
    OAUTH1("/oauth1"),
    API("/api"),
    CART("/cart/v1"),
    MATERIALS("/materials/v1"),
    MODELS("/models/v1"),
    PRINTERS("/printers/v1"),
    PRICE("/price/v1"),
    CATEGORY("/categories/v1"),
    ORDER("/orders/v1");

    private String value;

    Discovery(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return ShapewaysApi.BASE_URL + value;
    }
}
