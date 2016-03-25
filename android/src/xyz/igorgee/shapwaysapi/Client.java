package xyz.igorgee.shapwaysapi;

import android.util.Base64;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import xyz.igorgee.utilities.JavaUtilities;


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

    public String uploadModel(File file, String filename) {
        request = new OAuthRequest(Verb.POST, Discovery.MODELS.toString(), service);
        JSONObject json = createJSONObject(file, filename);
        request.addPayload(json.toString());
        service.signRequest(accessToken, request);
        return request.send().getBody();
    }

    private JSONObject createJSONObject(File file, String filename) {
        JSONObject json = new JSONObject();
        try {
            String encodedFile = Base64.encodeToString(JavaUtilities.loadFileAsBytesArray(file), Base64.DEFAULT);
            String urlEncoded = null;
            try {
                urlEncoded = URLEncoder.encode(encodedFile, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            json.put("file", urlEncoded);
            json.put("fileName", filename);
            json.put("hasRightsToModel", 1);
            json.put("acceptTermsAndConditions", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(json);
        return json;
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
