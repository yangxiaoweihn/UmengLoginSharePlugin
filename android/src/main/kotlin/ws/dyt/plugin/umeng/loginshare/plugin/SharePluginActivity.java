package ws.dyt.plugin.umeng.loginshare.plugin;

import android.os.Handler;
import android.os.Message;

import com.umeng.socialize.bean.SHARE_MEDIA;

import ws.dyt.plugin.umeng.loginshare.main.ShareMonitorActivity;

/**
 * 三方分享的插件回调监听
 * 需要回调结果给host
 *
 * 这么做的目的是在跨平台插件与原始模块做分离，为了避免代码的污染
 */
public class SharePluginActivity extends ShareMonitorActivity {

    private static final String TAG = "LoginPluginActivity";

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        super.onResult(share_media);

        this.sendMessage(Code.CODE_SUCCESS, null);
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        super.onCancel(share_media);

        this.sendMessage(Code.CODE_CANCEL, null);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        super.onError(share_media, throwable);

        this.sendMessage(Code.CODE_ERROR, throwable.getMessage());
    }

    private void sendMessage(int code, String data) {
        Handler handler = SharePlugin.resultHandler;
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
