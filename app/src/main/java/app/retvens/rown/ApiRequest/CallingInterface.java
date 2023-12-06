package app.retvens.rown.ApiRequest;

import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse;
import app.retvens.rown.api.MesiboCall;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @POST("sendGroupNotificationWithoutImage")
    Call<UpdateResponse> groupChatNotification(
            @Body GroupChatDataClass body
    );

    @Multipart
    @POST("sendAppNotification/{userId}")
    Call<UpdateResponse> imageNotification(
            @Path("userId") String groupId,
            @Part("title") RequestBody title,
            @Part("body") RequestBody body,
            @Part MultipartBody.Part Profile_pic
    );

    @Multipart
    @POST("sendGroupNotification")
    Call<UpdateResponse>imageGroupNotification(
            @Part("title") RequestBody title,
            @Part("body") RequestBody body,
            @Part("group_id") RequestBody groupId,
            @Part("sender_user_id") RequestBody sender,
            @Part MultipartBody.Part Profile_pic
    );


}
