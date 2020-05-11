package com.suning.speaker.httplibrary;
/**
 * @User: 李扬
 * @data: 2019/10/17
 */
public class DebugOrRelease {
    private static int uploadClooudLog = 0;
    private static Env env;

    public DebugOrRelease() {
    }

    public static Env getDebugOrRelease() {
        return env;
    }

    public static void setDebugOrRelease(Env var0) {
        env = var0;
    }

    public static void setUploadLog(int var0) {
        uploadClooudLog = var0;
    }

    public static int getUploadLog() {
        return uploadClooudLog;
    }

    static {
        env = Env.PRD;
    }
}
