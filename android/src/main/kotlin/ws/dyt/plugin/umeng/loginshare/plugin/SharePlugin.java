package ws.dyt.plugin.umeng.loginshare.plugin;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import ws.dyt.plugin.umeng.loginshare.entity.ShareParam;
import ws.dyt.plugin.umeng.loginshare.main.ActionDispatchMonitorReceiver;
import ws.dyt.plugin.umeng.loginshare.main.PlatSupportCheck;
import ws.dyt.plugin.umeng.loginshare.utils.LoginShareLog;
import ws.dyt.plugin.umeng.loginshare.utils.ObjectFind;

/**
 * Created by yangxiaowei on 2018/2/19.
 *
 * 与flutter分享数据协议:
 * plat: String     WeiXin|WeiXin_Circle|QZone|QQ|Sina
 * data: json
 *      Type: String    text|image|web[link|music|video]
 */

public class SharePlugin implements MethodChannel.MethodCallHandler{
    private static final String TAG = "SharePlugin";
    private static final String channel = "dyt.ws.plugin/share";

    public static void registerWith(PluginRegistry.Registrar registrar) {
        final MethodChannel mc = new MethodChannel(registrar.messenger(), channel);
        mc.setMethodCallHandler(new SharePlugin(registrar));
    }

    private SharePlugin(PluginRegistry.Registrar registrar) {
        this.registrar = registrar;
    }

    private final PluginRegistry.Registrar registrar;

    static SharePlugin.ResultHandler resultHandler;
    @Override
    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {

        resultHandler = new SharePlugin.ResultHandler(result);
        String method = methodCall.method;
        Object obj = methodCall.argument("plat");
        String plat = null == obj ? null : obj.toString().toUpperCase();

        LoginShareLog.d(TAG, "channel active -> method: "+method+" , arg: "+methodCall.argument("plat")+" , data: "+methodCall.argument("data"));
        if (method.equals("doShare")) {
            if (!PlatSupportCheck.checkPlatSupportForShare(plat)) {
                LoginShareLog.e(TAG, String.format("Does not support [%s]", plat));
                result.error(TAG, plat+" does not support", ResultProtocolForHost.param);
                return;
            }
            Object object = methodCall.argument("data");
            String data = null == object ? null : object.toString();
            if (TextUtils.isEmpty(data)) {
                LoginShareLog.e(TAG, "must need `data` field");
                result.error(TAG, "must need `data` field", ResultProtocolForHost.param);
                return;
            }
            this.doShare(
                    registrar.context(),
                    plat,
                    data,
                    SharePluginActivity.class.getName()
            );
        }
    }

    private void doShare(Context context, String plat, String data, String callBackActivity) {
        ShareParam param = this.convertCrossPlatData(plat, data);
        if (null == param) {
            return;
        }
        Intent intent = new Intent(context, ActionDispatchMonitorReceiver.class);
        intent.putExtra("plat", plat);
        intent.putExtra("data", param);
        intent.putExtra("callback_activity", callBackActivity);
        intent.setAction("ws.dyt.lib#umeng.share.data.ACTION");
        context.sendBroadcast(intent);
    }

    /**
     * 将flutter传过来的数据进行处理
     * @param plat
     * @param dataJson
     */
    private ShareParam convertCrossPlatData(String plat, String dataJson) {
        ShareParam param = null;
        //分享数据全部封装在data字段内，格式为json
        try {
            JSONObject object = new JSONObject(dataJson);
            param = new ShareParam(plat, ObjectFind.getString(object,"type"));
            param.content = ObjectFind.getString(object,"content");
            param.url = ObjectFind.getString(object,"url");
            param.thumbUrl = ObjectFind.getString(object,"thumbUrl");
            param.title = ObjectFind.getString(object,"title");
            param.description = ObjectFind.getString(object,"description");

            if (!param.validate()) {
                resultHandler.result.error(TAG, "need `plat` and `type` field", ResultProtocolForHost.param);
                return null;
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }

        return param;
    }

    private static class ResultHandler extends Handler {
        private MethodChannel.Result result;

        ResultHandler(MethodChannel.Result result) {
            this.result = result;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            LoginShareLog.d(TAG, "receive message: "+msg.what);

            //以下结构是跨端调用回调，结构不可更改
            int code = msg.what;
            if (code == Code.CODE_SUCCESS) {
                result.success(ResultProtocolForHost.success);
            }else if (code == Code.CODE_ERROR) {
                result.error(TAG, "The third part share error", ResultProtocolForHost.error);
            }else if (code == Code.CODE_CANCEL) {
                result.error(TAG, "The third part share cancel", ResultProtocolForHost.cancel);
            }else {
                result.notImplemented();
            }
        }
    }
}
