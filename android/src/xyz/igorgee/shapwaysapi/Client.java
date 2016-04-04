package xyz.igorgee.shapwaysapi;

import android.util.Log;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONObject;

import java.io.File;

import xyz.igorgee.utilities.CreateJson;


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

    public Token getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
    }

    public void retrieveAccessToken() {
        accessToken = service.getAccessToken(requestToken, verifier);
    }

    public Response uploadModel(File file, String filename) {
        request = new OAuthRequest(Verb.POST, Discovery.MODELS.toString(), service);
        JSONObject json = CreateJson.uploadFile(file, filename);
        request.addPayload(json.toString());
        return responseTo(request);
    }

    public Response addToCart(int modelId) {
        request = new OAuthRequest(Verb.POST, Discovery.CART.toString(), service);
        JSONObject json = CreateJson.addToCart(modelId);
        request.addPayload(json.toString());
        return responseTo(request);
    }

    public Response responseTo(OAuthRequest request) {
        service.signRequest(accessToken, request);
        Response response = request.send();

        String responseFormat = "Code: %s %s\nHeaders:\n%s\nBody:\n%s";
        Log.d("REQUEST", request.toString());
        Log.d("HTTP-LOG", String.format(responseFormat,
                response.getCode(), response.getMessage(),
                response.getHeaders(),
                response.getBody()));

        return response;
    }
}
