
package xyz.igorgee.Api.Model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Material {

    private Integer materialId;
    private Float markup;
    private Integer isActive;
    private Integer price;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Material(Integer materialId, Float markup, Integer isActive) {
        this.materialId = materialId;
        this.markup = markup;
        this.isActive = isActive;
    }

    /**
     * 
     * @return
     *     The materialId
     */
    public Integer getMaterialId() {
        return materialId;
    }

    /**
     * 
     * @param materialId
     *     The materialId
     */
    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    /**
     * 
     * @return
     *     The markup
     */
    public Float getMarkup() {
        return markup;
    }

    /**
     *
     * @param markup
     *     The markup
     */
    public void setMarkup(Float markup) {
        this.markup = markup;
    }

    /**
     * 
     * @return
     *     The isActive
     */
    public Integer getIsActive() {
        return isActive;
    }

    /**
     * 
     * @param isActive
     *     The isActive
     */
    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    /**
     * 
     * @return
     *     The price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * 
     * @param price
     *     The price
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
