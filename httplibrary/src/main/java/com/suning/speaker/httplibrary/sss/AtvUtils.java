package com.suning.speaker.httplibrary.sss;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class AtvUtils {
    public static Context sContext;
    /**
     * 获得版本信息
     *
     * @return
     */
    public static String getAppVersionName() {
        PackageInfo info = initPackInfo();
        return (null != info && !TextUtils.isEmpty(info.versionName)) ? info.versionName : "";
    }
    /**
     * 初始化包信息packInfo
     *
     * @return
     */
    public static PackageInfo initPackInfo() {
        PackageManager packageManager = sContext.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager != null ? packageManager.getPackageInfo(sContext
                    .getPackageName(), 0) : null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo;
    }
    public static String getMacAddress() {
//        return "0008226AD6FB";
        String addr =getWifiMacAddress();

        addr = addr.replace(":","");
        return addr;
    }
    public static String getMacAddress_1() {
//        String addr = getMacAddrInFile("/sys/class/net/eth0/address");
        String addr =getWifiMacAddress();
        return addr;
    }
    private static String getWifiMacAddress() {
        String defaultMac = "02:00:00:00:00:00";
        try {
            List<NetworkInterface> interfaces = Collections
                    .list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ntwInterface : interfaces) {

                if (ntwInterface.getName().equalsIgnoreCase("wlan0")) {//之前是p2p0，修正为wlan
                    byte[] byteMac = ntwInterface.getHardwareAddress();
                    if (byteMac == null) {
                        // return null;
                    }
                    StringBuilder strBuilder = new StringBuilder();
                    for (int i = 0; i < byteMac.length; i++) {
                        strBuilder.append(String
                                .format("%02X:", byteMac[i]));
                    }

                    if (strBuilder.length() > 0) {
                        strBuilder.deleteCharAt(strBuilder.length() - 1);
                    }

                    return strBuilder.toString();
                }

            }
        } catch (Exception e) {
//             Log.d(TAG, e.getMessage());
        }
        return defaultMac;
    }
    public static String getLocalEthernetMacAddress() {
        String mac = getMac("eth0");
        return mac;
    }
    public static String getWifiMacAddress(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        } catch (Exception e) {
        }
        return "";
    }
    public static String getWifiMac() {
        String wifiMac =getMac("wlan0");

        return wifiMac == null ? "" : wifiMac;
    }
    public static String getMac(String netName) {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase(netName)) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString().toLowerCase();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static String getSystemPropty(String prop) {
        Class<?> classType = null;
        Method getMethod = null;
        String ret = null;
        try {
            if (classType == null) {
                classType = Class.forName("android.os.SystemProperties");
                getMethod = classType.getDeclaredMethod("get", String.class);
            }
            ret = (String) getMethod.invoke(classType, prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
    public static String getSystemPropty(String prop, String defaultValue) {
        if (TextUtils.isEmpty(getSystemPropty(prop))) {
            return defaultValue;
        } else {
            return getSystemPropty(prop);
        }
    }
}
