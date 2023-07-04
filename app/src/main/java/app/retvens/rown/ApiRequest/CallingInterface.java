package app.retvens.rown.ApiRequest;

import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CallingInterface {
    @POST("call/{receiveruserId}")
    Call<CallResponse> calling(
            @Path("receiveruserId") String receiveruserId,
            @Body CallDataClass senderId
    );

    @POST("chating/{userId}")
    Call<UpdateResponse> chatNotification(
            @Path("userId") String userId,
            @Body ChatDataClass body
    );
}
