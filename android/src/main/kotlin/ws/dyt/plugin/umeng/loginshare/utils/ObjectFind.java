package ws.dyt.plugin.umeng.loginshare.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yangxiaowei
 * @date 2018/05/27
 */
public class ObjectFind {

    public static String getString(JSONObject object, String key) throws JSONException {
        if (null == object || null == key) {
            return null;
        }

        return object.has(key) ? object.getString(key) : null;
    }
}
