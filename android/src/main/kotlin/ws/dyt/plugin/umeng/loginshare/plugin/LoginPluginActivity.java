package ws.dyt.plugin.umeng.loginshare.plugin;

import android.os.Handler;
import android.os.Message;

import com.umeng.socialize.bean.SHARE_MEDIA;

import ws.dyt.plugin.umeng.loginshare.main.LoginMonitorActivity;

/**
 * 三方登录的插件回调监听
 * 需要回调结果给host
 *
 * 这么做的目的是在跨平台插件与原始模块做分离，为了避免代码的污染
 */
public class LoginPluginActivity extends LoginMonitorActivity {
    private static final String TAG = "LoginPluginActivity";

    @Override
    protected void onComplete(SHARE_MEDIA share_media, String json) {
        super.onComplete(share_media, json);

        this.sendMessage(Code.CODE_SUCCESS, json);
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        super.onCancel(share_media, i);

        this.sendMessage(Code.CODE_CANCEL, null);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        super.onError(share_media, i, throwable);

        this.sendMessage(Code.CODE_ERROR, throwable.getMessage());
    }

    private void sendMessage(int code, String data) {
        Handler handler = LoginPlugin.resultHandler;
        Message message = Message.obtain();
        message.what = code;
        message.obj = data;
        handler.sendMessage(message);

        this.dismiss();
    }

    private void dismiss() {
        finish();
    }
}
