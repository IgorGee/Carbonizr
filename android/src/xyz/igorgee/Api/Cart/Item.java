
package xyz.igorgee.Api.Cart;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Item {

    private String cartKey;
    private String modelId;
    private String spin;
    private String materialId;
    private String quantity;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The cartKey
     */
    public String getCartKey() {
        return cartKey;
    }

    /**
     * 
     * @param cartKey
     *     The cartKey
     */
    public void setCartKey(String cartKey) {
        this.cartKey = cartKey;
    }

    /**
     * 
     * @return
     *     The modelId
     */
    public String getModelId() {
        return modelId;
    }

    /**
     * 
     * @param modelId
     *     The modelId
     */
    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    /**
     * 
     * @return
     *     The spin
     */
    public String getSpin() {
        return spin;
    }

    /**
     * 
     * @param spin
     *     The spin
     */
    public void setSpin(String spin) {
        this.spin = spin;
    }

    /**
     * 
     * @return
     *     The materialId
     */
    public String getMaterialId() {
        return materialId;
    }

    /**
     * 
     * @param materialId
     *     The materialId
     */
    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * 
     * @return
     *     The quantity
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * 
     * @param quantity
     *     The quantity
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
