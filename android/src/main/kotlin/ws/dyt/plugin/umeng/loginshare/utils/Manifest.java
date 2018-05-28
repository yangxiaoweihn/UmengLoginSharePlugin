package ws.dyt.plugin.umeng.loginshare.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by yangxiaowei on 2018/2/15.
 */

public class Manifest {

    public static <T> T getMeta(Context context, String key) {
        return getMeta(context, key, null);
    }

    public static <T> T getMeta(Context context, String key, T def) {
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (null == info) {
            return null;
        }

        Object o = info.metaData.get(key);

        return o != null ? (T) o : def;
    }
}
