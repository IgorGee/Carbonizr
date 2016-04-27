package xyz.igorgee.utilities;

public class ModelToUpload {
    String file;
    String fileName;
    boolean hasRightsToModel;
    boolean acceptTermsAndConditions;
    String title;
    boolean isPublic;
    int isForSale;
    boolean isDownloadable;

    public ModelToUpload(String file, String fileName, boolean hasRightsToModel, boolean acceptTermsAndConditions) {
        this.file = file;
        this.fileName = fileName;
        this.hasRightsToModel = hasRightsToModel;
        this.acceptTermsAndConditions = acceptTermsAndConditions;
        this.isPublic = true;
        this.isForSale = 0; // Why is this an int and not a boolean lol?
        this.isDownloadable = false;
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
}
