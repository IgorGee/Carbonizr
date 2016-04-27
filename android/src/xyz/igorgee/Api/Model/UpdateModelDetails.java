
package xyz.igorgee.Api.Model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class UpdateModelDetails {

    private String method;
    private String restUrl;
    private String link;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The method
     */
    public String getMethod() {
        return method;
    }

    /**
     * 
     * @param method
     *     The method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * 
     * @return
     *     The restUrl
     */
    public String getRestUrl() {
        return restUrl;
    }

    /**
     * 
     * @param restUrl
     *     The restUrl
     */
    public void setRestUrl(String restUrl) {
        this.restUrl = restUrl;
    }

    /**
     * 
     * @return
     *     The link
     */
    public String getLink() {
        return link;
    }

    /**
     * 
     * @param link
     *     The link
     */
    public void setLink(String link) {
        this.link = link;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
