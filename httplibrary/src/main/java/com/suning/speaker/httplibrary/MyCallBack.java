package com.suning.speaker.httplibrary;

import android.content.Context;
import android.provider.FontRequest;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * /**
 *
 * @User: 李扬
 * @data: 2019/10/17
 * 网络请求返回体封装
 */
public abstract class MyCallBack<T> implements Callback<ResponseBody> {
    private Context activity;
    public Type type;

    public abstract void success(T t);

    public abstract void failue(int code, String msg);

//    public abstract void reRequest();

    public MyCallBack(Context activity) {
        this.activity = activity;
        Type mySuperClass = getClass().getGenericSuperclass();
        type = ((ParameterizedType) mySuperClass).getActualTypeArguments()[0];
    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        ResponseBody body = response.body();
        if (response.isSuccessful() && body != null) {
            responseBody(body);
            return;
        }
        handleCode(-1, "");
    }

    private void responseBody(ResponseBody body) {
        try {
            String result = body.string();
            JSONObject response = new JSONObject(result);
            int code = response.optInt("code", -1);
            String desc = response.optString("responseMsg", "");
            int res_code = response.optInt("res_code");
            if (res_code == 0 || code == 1000) {
                if (type.hashCode() == String.class.hashCode()) {
                    success((T) result);
                    return;
                }
                Gson gson = new Gson();
                T t = gson.fromJson(result, type);
                success(t);
                return;
            }
            handleCode(res_code, desc);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            handleCode(-1, "");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {


        if (t == null) {
            handleCode(-1, "");
            return;
        }
        if ((t.toString().contains("TimeoutException")) || "timeout".equalsIgnoreCase(t.getMessage())) {

            handleCode(-2, "网络连接超时！");
//            client.newCall(call.request()).enqueue(this);
            return;
        }
        if (t.toString().contains("java.net.UnknownHostException") || t.toString().contains("ConnectException") || t.toString().contains("java.net")) {
            handleCode(-3, "网络不给力~");
            return;
        }

    }

    public void handleCode(int code, String err_msg) {
        failue(code, err_msg);
    }
}
