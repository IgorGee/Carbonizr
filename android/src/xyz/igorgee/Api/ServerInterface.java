package xyz.igorgee.Api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import xyz.igorgee.Api.Model.ModelResponse;
import xyz.igorgee.utilities.ModelToUpload;

public interface ServerInterface {
    @POST("model")
    Call<ModelResponse> uploadToShop(@Body ModelToUpload modelToUpload);
}
