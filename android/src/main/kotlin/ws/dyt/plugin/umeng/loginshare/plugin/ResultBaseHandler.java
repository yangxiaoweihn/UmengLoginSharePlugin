package ws.dyt.plugin.umeng.loginshare.plugin;

import android.os.Handler;

import java.util.HashMap;
import java.util.Map;
import io.flutter.plugin.common.MethodChannel;

/**
 * @author yangxiaowei
 */
abstract
public class ResultBaseHandler extends Handler {

    protected MethodChannel.Result result;

    ResultBaseHandler(MethodChannel.Result result) {
        this.result = result;
    }

    /**
     * 错误消息封装
     * @param code
     * @param data
     * @return
     */
    protected Map<String, Object> errorData(@ResultProtocolForHost int code, Object data) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("code", code);
        map.put("data", data);
        return map;
    }
}
