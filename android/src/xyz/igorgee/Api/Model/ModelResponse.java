
package xyz.igorgee.Api.Model;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ModelResponse {

    private String result;
    private Integer modelId;
    private Integer modelVersion;
    private String title;
    private String fileName;
    private Integer contentLength;
    private String fileMd5Checksum;
    private String description;
    private Integer isPublic;
    private Integer isClaimaBle;
    private Boolean isForSale;
    private Integer isDownloadable;
    private String secretKey;
    private String claimKey;
    private String defaultMAterialId;
    private Urls urls;
    private String spin;
    private String printable;
    private NextActionSuggestions nextActionSuggestions;
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
     *     The modelId
     */
    public Integer getModelId() {
        return modelId;
    }

    /**
     * 
     * @param modelId
     *     The modelId
     */
    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    /**
     * 
     * @return
     *     The modelVersion
     */
    public Integer getModelVersion() {
        return modelVersion;
    }

    /**
     * 
     * @param modelVersion
     *     The modelVersion
     */
    public void setModelVersion(Integer modelVersion) {
        this.modelVersion = modelVersion;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 
     * @param fileName
     *     The fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 
     * @return
     *     The contentLength
     */
    public Integer getContentLength() {
        return contentLength;
    }

    /**
     * 
     * @param contentLength
     *     The contentLength
     */
    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * 
     * @return
     *     The fileMd5Checksum
     */
    public String getFileMd5Checksum() {
        return fileMd5Checksum;
    }

    /**
     * 
     * @param fileMd5Checksum
     *     The fileMd5Checksum
     */
    public void setFileMd5Checksum(String fileMd5Checksum) {
        this.fileMd5Checksum = fileMd5Checksum;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The isPublic
     */
    public Integer getIsPublic() {
        return isPublic;
    }

    /**
     * 
     * @param isPublic
     *     The isPublic
     */
    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    /**
     * 
     * @return
     *     The isClaimaBle
     */
    public Integer getIsClaimaBle() {
        return isClaimaBle;
    }

    /**
     * 
     * @param isClaimaBle
     *     The isClaima    ble
     */
    public void setIsClaimaBle(Integer isClaimaBle) {
        this.isClaimaBle = isClaimaBle;
    }

    /**
     * 
     * @return
     *     The isForSale
     */
    public Boolean getIsForSale() {
        return isForSale;
    }

    /**
     * 
     * @param isForSale
     *     The isForSale
     */
    public void setIsForSale(Boolean isForSale) {
        this.isForSale = isForSale;
    }

    /**
     * 
     * @return
     *     The isDownloadable
     */
    public Integer getIsDownloadable() {
        return isDownloadable;
    }

    /**
     * 
     * @param isDownloadable
     *     The isDownloadable
     */
    public void setIsDownloadable(Integer isDownloadable) {
        this.isDownloadable = isDownloadable;
    }

    /**
     * 
     * @return
     *     The secretKey
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * 
     * @param secretKey
     *     The secretKey
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * 
     * @return
     *     The claimKey
     */
    public String getClaimKey() {
        return claimKey;
    }

    /**
     * 
     * @param claimKey
     *     The claimKey
     */
    public void setClaimKey(String claimKey) {
        this.claimKey = claimKey;
    }

    /**
     * 
     * @return
     *     The defaultMAterialId
     */
    public String getDefaultMAterialId() {
        return defaultMAterialId;
    }

    /**
     * 
     * @param defaultMAterialId
     *     The defaultM    aterialId
     */
    public void setDefaultMAterialId(String defaultMAterialId) {
        this.defaultMAterialId = defaultMAterialId;
    }

    /**
     * 
     * @return
     *     The urls
     */
    public Urls getUrls() {
        return urls;
    }

    /**
     * 
     * @param urls
     *     The urls
     */
    public void setUrls(Urls urls) {
        this.urls = urls;
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
     *     The printable
     */
    public String getPrintable() {
        return printable;
    }

    /**
     * 
     * @param printable
     *     The printable
     */
    public void setPrintable(String printable) {
        this.printable = printable;
    }

    /**
     * 
     * @return
     *     The nextActionSuggestions
     */
    public NextActionSuggestions getNextActionSuggestions() {
        return nextActionSuggestions;
    }

    /**
     * 
     * @param nextActionSuggestions
     *     The nextActionSuggestions
     */
    public void setNextActionSuggestions(NextActionSuggestions nextActionSuggestions) {
        this.nextActionSuggestions = nextActionSuggestions;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
