
package xyz.igorgee.Api.Model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class NextActionSuggestions {

    private GetModel getModel;
    private DownloadModel downloadModel;
    private UpdateModelDetails updateModelDetails;
    private UpdateModelFile updateModelFile;
    private AddModelPhoto addModelPhoto;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The getModel
     */
    public GetModel getGetModel() {
        return getModel;
    }

    /**
     * 
     * @param getModel
     *     The getModel
     */
    public void setGetModel(GetModel getModel) {
        this.getModel = getModel;
    }

    /**
     * 
     * @return
     *     The downloadModel
     */
    public DownloadModel getDownloadModel() {
        return downloadModel;
    }

    /**
     * 
     * @param downloadModel
     *     The downloadModel
     */
    public void setDownloadModel(DownloadModel downloadModel) {
        this.downloadModel = downloadModel;
    }

    /**
     * 
     * @return
     *     The updateModelDetails
     */
    public UpdateModelDetails getUpdateModelDetails() {
        return updateModelDetails;
    }

    /**
     * 
     * @param updateModelDetails
     *     The updateModelDetails
     */
    public void setUpdateModelDetails(UpdateModelDetails updateModelDetails) {
        this.updateModelDetails = updateModelDetails;
    }

    /**
     * 
     * @return
     *     The updateModelFile
     */
    public UpdateModelFile getUpdateModelFile() {
        return updateModelFile;
    }

    /**
     * 
     * @param updateModelFile
     *     The updateModelFile
     */
    public void setUpdateModelFile(UpdateModelFile updateModelFile) {
        this.updateModelFile = updateModelFile;
    }

    /**
     * 
     * @return
     *     The addModelPhoto
     */
    public AddModelPhoto getAddModelPhoto() {
        return addModelPhoto;
    }

    /**
     * 
     * @param addModelPhoto
     *     The addModelPhoto
     */
    public void setAddModelPhoto(AddModelPhoto addModelPhoto) {
        this.addModelPhoto = addModelPhoto;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
