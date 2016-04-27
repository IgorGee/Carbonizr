package xyz.igorgee.utilities;

import java.util.HashMap;
import java.util.Map;

import xyz.igorgee.Api.Model.Material;
import xyz.igorgee.Api.Materials;

public class ModelToUpload {
    String file;
    String fileName;
    Boolean hasRightsToModel;
    Boolean acceptTermsAndConditions;
    String title;
    Boolean isPublic;
    Integer isForSale;
    Boolean isDownloadable;
    Map<String, Material> materials;
    Integer defaultMaterialId;

    public ModelToUpload(String file, String fileName, boolean hasRightsToModel, boolean acceptTermsAndConditions) {
        this.file = file;
        this.fileName = fileName;
        this.hasRightsToModel = hasRightsToModel;
        this.acceptTermsAndConditions = acceptTermsAndConditions;
        this.isPublic = true;
        this.isForSale = 1; // Why is this an int and not a boolean lol?
        this.isDownloadable = false;
        this.defaultMaterialId = 6;

        materials = new HashMap<>();
        for (Materials m : Materials.values()) {
            materials.put(String.valueOf(m.getMaterialId()),
                    new Material(m.getMaterialId(), 1.00f, 1));
        }
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isHasRightsToModel() {
        return hasRightsToModel;
    }

    public void setHasRightsToModel(boolean hasRightsToModel) {
        this.hasRightsToModel = hasRightsToModel;
    }

    public boolean isAcceptTermsAndConditions() {
        return acceptTermsAndConditions;
    }

    public void setAcceptTermsAndConditions(boolean acceptTermsAndConditions) {
        this.acceptTermsAndConditions = acceptTermsAndConditions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public int getIsForSale() {
        return isForSale;
    }

    public void setIsForSale(int isForSale) {
        this.isForSale = isForSale;
    }

    public boolean isDownloadable() {
        return isDownloadable;
    }

    public void setIsDownloadable(boolean isDownloadable) {
        this.isDownloadable = isDownloadable;
    }

    public Map<String, Material> getMaterials() {
        return materials;
    }

    public void setMaterials(Map<String, Material> materials) {
        this.materials = materials;
    }

    public Integer getDefaultMaterialId() {
        return defaultMaterialId;
    }

    public void setDefaultMaterialId(Integer defaultMaterialId) {
        this.defaultMaterialId = defaultMaterialId;
    }
}
