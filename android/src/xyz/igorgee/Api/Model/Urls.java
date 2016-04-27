
package xyz.igorgee.Api.Model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Urls {

    private PrivateProductUrl privateProductUrl;
    private PublicProductUrl publicProductUrl;
    private EditModelUrl editModelUrl;
    private EditProduCtDetailsUrl editProduCtDetailsUrl;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The privateProductUrl
     */
    public PrivateProductUrl getPrivateProductUrl() {
        return privateProductUrl;
    }

    /**
     * 
     * @param privateProductUrl
     *     The privateProductUrl
     */
    public void setPrivateProductUrl(PrivateProductUrl privateProductUrl) {
        this.privateProductUrl = privateProductUrl;
    }

    /**
     * 
     * @return
     *     The publicProductUrl
     */
    public PublicProductUrl getPublicProductUrl() {
        return publicProductUrl;
    }

    /**
     * 
     * @param publicProductUrl
     *     The publicProductUrl
     */
    public void setPublicProductUrl(PublicProductUrl publicProductUrl) {
        this.publicProductUrl = publicProductUrl;
    }

    /**
     * 
     * @return
     *     The editModelUrl
     */
    public EditModelUrl getEditModelUrl() {
        return editModelUrl;
    }

    /**
     * 
     * @param editModelUrl
     *     The editModelUrl
     */
    public void setEditModelUrl(EditModelUrl editModelUrl) {
        this.editModelUrl = editModelUrl;
    }

    /**
     * 
     * @return
     *     The editProduCtDetailsUrl
     */
    public EditProduCtDetailsUrl getEditProduCtDetailsUrl() {
        return editProduCtDetailsUrl;
    }

    /**
     * 
     * @param editProduCtDetailsUrl
     *     The editProdu    ctDetailsUrl
     */
    public void setEditProduCtDetailsUrl(EditProduCtDetailsUrl editProduCtDetailsUrl) {
        this.editProduCtDetailsUrl = editProduCtDetailsUrl;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
