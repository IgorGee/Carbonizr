
package xyz.igorgee.Api.Cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Cart {

    private String result;
    private Integer itemCount;
    private List<Item> items = new ArrayList<Item>();
    private CartItems cartItems;
    private Integer cartItemCount;
    private List<Object> nextActionSuggestions = new ArrayList<Object>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The result
     */
    public String getResult() {
        return result;
    }

    /**
     * 
     * @param result
     *     The result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * 
     * @return
     *     The itemCount
     */
    public Integer getItemCount() {
        return itemCount;
    }

    /**
     * 
     * @param itemCount
     *     The itemCount
     */
    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    /**
     * 
     * @return
     *     The items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * 
     * @param items
     *     The items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * 
     * @return
     *     The cartItems
     */
    public CartItems getCartItems() {
        return cartItems;
    }

    /**
     * 
     * @param cartItems
     *     The cartItems
     */
    public void setCartItems(CartItems cartItems) {
        this.cartItems = cartItems;
    }

    /**
     * 
     * @return
     *     The cartItemCount
     */
    public Integer getCartItemCount() {
        return cartItemCount;
    }

    /**
     * 
     * @param cartItemCount
     *     The cartItemCount
     */
    public void setCartItemCount(Integer cartItemCount) {
        this.cartItemCount = cartItemCount;
    }

    /**
     * 
     * @return
     *     The nextActionSuggestions
     */
    public List<Object> getNextActionSuggestions() {
        return nextActionSuggestions;
    }

    /**
     * 
     * @param nextActionSuggestions
     *     The nextActionSuggestions
     */
    public void setNextActionSuggestions(List<Object> nextActionSuggestions) {
        this.nextActionSuggestions = nextActionSuggestions;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
