package ws.dyt.plugin.umeng.loginshare.plugin;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import ws.dyt.plugin.umeng.loginshare.main.ActionDispatchMonitorReceiver;
import ws.dyt.plugin.umeng.loginshare.main.PlatSupportCheck;
import ws.dyt.plugin.umeng.loginshare.utils.LoginShareLog;

/**
 * Created by yangxiaowei on 2018/2/19.
 */

public class LoginPlugin implements MethodChannel.MethodCallHandler{
    private static final String TAG = "LoginPlugin";
    private static final String channel = "dyt.ws.plugin/login";

    public static void registerWith(PluginRegistry.Registrar registrar) {
        final MethodChannel mc = new MethodChannel(registrar.messenger(), channel);
        mc.setMethodCallHandler(new LoginPlugin(registrar));
    }

    private LoginPlugin(PluginRegistry.Registrar registrar) {
        this.registrar = registrar;
    }

    private final PluginRegistry.Registrar registrar;

    static ResultHandler resultHandler;
    @Override
    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {

        resultHandler = new ResultHandler(result);
        String method = methodCall.method;
        LoginShareLog.d(TAG, "channel active -> method: "+method+" , arg: "+methodCall.argument("plat"));
        if (method.equals("doLogin")) {
            Object obj = methodCall.argument("plat");
            String plat = null == obj ? null : obj.toString().toLowerCase();

            if (!PlatSupportCheck.checkPlatSupportForLogin(plat)) {
                LoginShareLog.e(TAG, String.format("Does not support [%s]", plat));
                result.error(TAG, plat+" does not support", new Object[]{ResultProtocolForHost.param, null});
                return;
            }
            this.doLogin(registrar.context(), plat, LoginPluginActivity.class.getName());
        }
    }

    private void doLogin(Context context, String plat, String callBackActivity) {

        LoginShareLog.d(TAG, "will start ShareMonitorService");
        Intent intent = new Intent(context, ActionDispatchMonitorReceiver.class);
        intent.putExtra("plat", plat);
        intent.putExtra("callback_activity", callBackActivity);
        intent.setAction("ws.dyt.lib#umeng.login.data.ACTION");
        context.sendBroadcast(intent);
    }

    private static class ResultHandler extends ResultBaseHandler {

        ResultHandler(MethodChannel.Result result) {
            super(result);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            LoginShareLog.d(TAG, "receive message: "+msg.what);

            //以下结构是跨端调用回调，结构不可更改
            int code = msg.what;
            if (code == Code.CODE_SUCCESS) {
                result.success(msg.obj);
            }else if (code == Code.CODE_ERROR) {
                result.error(TAG, "The third part login error", this.errorData(ResultProtocolForHost.error, msg.obj));
            }else if (code == Code.CODE_CANCEL) {
                result.error(TAG, "The third part login cancel", this.errorData(ResultProtocolForHost.cancel, null));
            }else {
                result.notImplemented();
            }
        }
    }
}
