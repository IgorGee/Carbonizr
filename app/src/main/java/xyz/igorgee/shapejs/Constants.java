package xyz.igorgee.shapejs;

public enum  Constants {
    JS_2D_TO_3D("function imgChanged(e){return void 0===e.img?null:(imgBox=new Image3D(e.img,20*MM,20*MM,4*MM,vs),imgBox.setBlurWidth(.1*MM),imgBox.setImagePlace(Image3D.IMAGE_PLACE_BOTH),imgBox.setBaseThickness(.5),imgBox.set(\"distanceFactor\",.8),shape.setSource(imgBox),null)}function main(e){var n=11*MM,i=new Box(2*n,2*n,4*MM);new Bounds(-n,n,-n,n,-n,n);return shape=new Scene(i,new Bounds(-n,n,-n,n,-n,n),vs),void 0===e.img?shape:(imgChanged(e),shape)}var uiParams=[{name:\"img\",desc:\"Image Source\",type:\"uri\",onChange:\"imgChanged\"}],vs=.1*MM,imgBox,shape;"),
    UPDATE_SCENE_ENDPOINT("https://gpu-public-us-east-1b.shapeways.com/service/sws_service_shapejs_rt_v1.0.0/updateScene"),
    SAVE_MODEL_CACHED_ENDPOINT("https://gpu-public-us-east-1b.shapeways.com/service/sws_service_shapejs_rt_v1.0.0/saveModelCached");

    private String value;

    Constants(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
