package com.suning.speaker.httplibrary;
import java.util.HashMap;
import java.util.Map;

/**
 * @User: 李扬
 * @data: 2019/10/17
 */

public class ApiFactory {


    public static HttpService createHttpService() {
        return provideService(getRootUrl(),HttpService.class);
    }
    /**
     * 根据设置环境获取根路径
     */
    private static String getRootUrl() {
        String mRootUrl = Domain.URL_PRD;
        Env rooturl = DebugOrRelease.getDebugOrRelease();
        if (rooturl == Env.PRD) {
            mRootUrl = Domain.URL_PRD;
        } else if (rooturl == Env.PRE) {
            mRootUrl = Domain.URL_PRE;
        } else if (rooturl == Env.SIT) {
            mRootUrl = Domain.URL_SIT;
        }
        return mRootUrl;
    }

    private static Map<Class, Object> m_service = new HashMap();

    private static <T> T provideService(String  url,Class cls) {
        Object serv = m_service.get(cls);
        if (serv == null) {
            synchronized (cls) {
                serv = m_service.get(cls);
                if (serv == null) {
                    serv = RestApi.getInstance().create(url, cls);
                    m_service.put(cls, serv);
                }
            }
        }
        return (T) serv;
    }
}
