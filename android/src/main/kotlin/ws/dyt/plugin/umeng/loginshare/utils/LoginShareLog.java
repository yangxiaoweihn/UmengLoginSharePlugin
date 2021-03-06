package ws.dyt.plugin.umeng.loginshare.utils;

import android.util.Log;

/**
 * Created by yangxiaowei 2018/05/27
 */
public class LoginShareLog {

    private volatile static boolean debug = true;
    public static void setDebug(boolean debug) {
        LoginShareLog.debug = debug;
    }

    public static void d(String tag, String message) {

        if (debug) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {

        if (debug) {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {

        if (debug) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {

        if (debug) {
            Log.e(tag, message);
        }
    }
}
