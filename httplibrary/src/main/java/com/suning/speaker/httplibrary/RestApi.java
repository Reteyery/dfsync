package com.suning.speaker.httplibrary;

import android.os.Build;

import com.suning.speaker.httplibrary.sss.AtvUtils;
import com.suning.speaker.httplibrary.sss.BuildSettings;
import com.suning.speaker.httplibrary.sss.CommonCookieJar;
import com.suning.speaker.httplibrary.sss.HttpsUtils;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * /**
 *
 * @User: 李扬
 * @data: 2019/10/17
 */
public class RestApi {
    public static final String BODY_CONTENT_TYPE = "application/x-www-form-urlencoded; charset=utf-8";
    private static RestApi mInstance;
    private final static int TIME_OUT = 10;
    private static CommonCookieJar cookieJar;
//    private static ClearableCookieJar cookieJar;
    private static Pattern loginPattern;
    public static synchronized RestApi getInstance() {
        if (mInstance == null){
            cookieJar = new CommonCookieJar();
//            cookieJar = new PersistentCookieJar(new SetCookieCache(),new SharedPrefsCookiePersistor(AtvUtils.sContext));
            mInstance = new RestApi();
            loginPattern = Pattern.compile("^https?://passport.+/ids/login.+$");
        }
        return mInstance;
    }

    private Retrofit createApiClient(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkHttpClient())
                .build();
    }

    public <T> T create(String baseUrl, Class<T> clz) {
        return createApiClient(baseUrl).create(clz);
    }

   private OkHttpClient createOkHttpClient() {
        try {
            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder build = chain.request().newBuilder();
                    //添加头
                    build.addHeader("Content-type", BODY_CONTENT_TYPE)
                      .addHeader("User-Agent", getUserAgentString());
                    Request newRequest = build.build();
                    Response response = chain.proceed(newRequest);
                    int code = response.code();
                    if (code == 200) {
                        String url = response.request().url().toString();
                        if (loginPattern.matcher(url).matches()) {
//                            EventBus.getDefault().post(new NotLoginEvent());
                        }
                    }
                    return response;
                }
            };
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)//trust all the https point
                    .addInterceptor(interceptor)
                    .cookieJar(cookieJar)
                    .followRedirects(true)
                    .followSslRedirects(true)
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier)
                    .hostnameVerifier((hostname, session) -> true).build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private String getUserAgentString() {
        StringBuilder sb = new StringBuilder("SmartHome/");
        sb.append(AtvUtils.getAppVersionName()).append("(").append(BuildSettings.ReleaseChannel)
                .append(";android ").append(android.os.Build.VERSION.SDK_INT).append(";")
                .append(android.os.Build.VERSION.RELEASE).append(")");
//        Log.d(TAG, "getUserAgentString UA==>>" + sb.toString());
        return sb.toString();
    }
    /** user_agent **/
    public static String USER_AGENT =
            "Mozilla/5.0(Linux; U;SNEBUY-APP;SNCLIENT; Android " +
                    Build.VERSION.RELEASE + "; " +
                    Locale.getDefault().getLanguage() + "; " + Build.MODEL +
                    ") AppleWebKit/533.0 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
}
