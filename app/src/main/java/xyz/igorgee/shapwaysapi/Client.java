package xyz.igorgee.shapwaysapi;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuth10aService;


public class Client {

    private OAuth10aService service;
    private Token requestToken;
    private Token accessToken;
    private Verifier verifier;
    private OAuthRequest request;

    public Client() {
        service = new ServiceBuilder()
                .apiKey(Consumer.KEY.toString())
                .apiSecret(Consumer.SECRET.toString())
                .build(ShapewaysApi.instance());
    }

    public String getAuthorizationUrl() {
        requestToken = service.getRequestToken();
        return service.getAuthorizationUrl(requestToken);
    }

    public void setVerifier(String pin) {
        verifier = new Verifier(pin);
    }

    public void setAccessToken() {
        accessToken = service.getAccessToken(requestToken, verifier);
    }

    public String getCart() {
        request = new OAuthRequest(Verb.GET, Discovery.CART.toString(), service);
        service.signRequest(accessToken, request);
        return request.send().getBody();
    }

    public String getOrder() {
        request = new OAuthRequest(Verb.GET, Discovery.ORDER.toString(), service);
        service.signRequest(accessToken, request);
        return request.send().getBody();
    }

}
