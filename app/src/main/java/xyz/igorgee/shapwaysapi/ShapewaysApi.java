package xyz.igorgee.shapwaysapi;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;

public class ShapewaysApi extends DefaultApi10a {

    public static final String BASE_URL = "https://api.shapeways.com";

    private static final String AUTHORIZATION_URL =
            "https://api.shapeways.com/authorize?oauth_token=%s";

    /**
     * Initialization on Demand Holder (Design Pattern)
     */
    private static class InstanceHolder {
        private static final ShapewaysApi INSTANCE = new ShapewaysApi();
    }

    /**
     * Lazy instantiation of the Shapeways API instance.
     *
     * @return The ShapewaysApi instance.
     */
    public static ShapewaysApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://api.shapeways.com/oauth1/request_token/v1";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.shapeways.com/oauth1/access_token/v1";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return  String.format(AUTHORIZATION_URL, requestToken.getToken());
    }
}
