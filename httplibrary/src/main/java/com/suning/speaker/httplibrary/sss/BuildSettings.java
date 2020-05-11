/**
 *
 */
package com.suning.speaker.httplibrary.sss;
/**
 * ant 分渠道打包用
 */
public class BuildSettings {

    private static final String TAG = BuildSettings.class.getSimpleName();
    public static final String DEFAULT_CHANNEL = "999999";

    public static String ReleaseChannel = DEFAULT_CHANNEL;

    /**
     * 初始化Channel
     */
    public static void initReleaseChannel() {
        String romChannel = AtvUtils.getSystemPropty("ro.product.channel", DEFAULT_CHANNEL);
        if (romChannel.length() == 6) {
            if (romChannel.startsWith("60001")) {
                ReleaseChannel = "160010";
            } else if (romChannel.startsWith("60002")) {
                ReleaseChannel = "160020";
            } else if (romChannel.startsWith("60003")) {
                ReleaseChannel = "160030";
            }
        }
    }
    public static String getChannel() {
        return ReleaseChannel;
    }


}
