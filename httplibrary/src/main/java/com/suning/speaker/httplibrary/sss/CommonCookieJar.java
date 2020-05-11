package com.suning.speaker.httplibrary.sss;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CommonCookieJar implements CookieJar {

    private Map<String, Cookie> cookieMap = new LinkedHashMap<>();

    public void setCookies(List<Cookie> cookies) {
//        if (BuildConfig.DEBUG) {
//            LogUtils.debug("Set cookies: " + cookies.size() + " items, " + ToStringProcess.toString(cookies));
//        }
        cookieMap.clear();
        for (Cookie cookie : cookies) {
            cookieMap.put(createCookieKey(cookie), cookie);
        }
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            cookieMap.put(createCookieKey(cookie), cookie);
        }
        Log.e("TTTTT---","-----------------------------------------");
        for (Cookie cookie : cookieMap.values()){
            Log.e("TTTTT---",cookie.toString());
//            Log.e("TTTTT---11",new Gson().toJson(cookie));
        }
        Log.e("TTTTT---","-----------------------------------------");
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> invalidCookies = new ArrayList<>();
        List<Cookie> validCookies = new ArrayList<>();
        for (Cookie cookie : cookieMap.values()) {
            if (cookie.expiresAt() < System.currentTimeMillis()) {
                invalidCookies.add(cookie);
            } else if (cookie.matches(url)) {
                validCookies.add(cookie);
            }
        }
        //移除无效的cookie
        for (Cookie invalidCookie : invalidCookies) {
            cookieMap.remove(createCookieKey(invalidCookie));
        }
        Log.e("TTTTT---","*****************************");
        for (Cookie cookie : cookieMap.values()){
            Log.e("TTTTT---",cookie.toString());
//            Log.e("TTTTT---11",new Gson().toJson(cookie));
        }
        Log.e("TTTTT---","*****************************");
        return validCookies;
    }

    private String createCookieKey(Cookie cookie) {
        return (cookie.secure() ? "https" : "http") + "://" + cookie.domain() + cookie.path() + "|" + cookie.name();
    }
}
