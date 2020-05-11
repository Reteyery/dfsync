package com.suning.speaker.httplibrary;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * @User: 李扬
 * @data: 2019/10/17
 */
public interface HttpService {

    @POST()
    Call<ResponseBody> getQrLoginUuidGenerate(@Url String url, @Query("image") boolean image);
    @POST()
    Call<ResponseBody>   getQrLoginStateProbeUrl(@Url String url,@Query("rememberMe")boolean rememberMe);
    @GET("api/device/smartscreenbox/getAppUrl")
    Call<ResponseBody>  getAppUrlUrl (@QueryMap Map<String,String> map);

    @GET()
    Call<ResponseBody>  QueryUserInfo (@Url String url, @QueryMap Map<String,String> map);

}
