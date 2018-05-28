package ws.dyt.plugin.umeng.loginshare.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import ws.dyt.plugin.umeng.loginshare.R;
import ws.dyt.plugin.umeng.loginshare.utils.LoginShareLog;
import ws.dyt.plugin.umeng.loginshare.utils.Manifest;

/**
 * Created by yangxiaowei
 *
 * 分享|登录初始化监听
 */
public class ModuleInitReceiver extends BroadcastReceiver {

    private final String TAG = getClass().getSimpleName();

    public ModuleInitReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LoginShareLog.e(TAG, "ModuleInitReceiver -> action: "+action);

        final String ACTION = context.getString(R.string.action_umeng_share_init);
        if (null != action && action.equals(ACTION)) {
            this.init(context);
        }
    }

    private void init(Context context) {

        fuckInit(context);

        LoginShareLog.e(TAG, Manifest.getMeta(context, "UMENG_APPKEY", null)+"");
        LoginShareLog.e(TAG, Manifest.getMeta(context, "QQ_APPKEY", null)+" , "+Manifest.getMeta(context, "QQ_APPSECRET", null));
        LoginShareLog.e(TAG, Manifest.getMeta(context, "WX_APPKEY", null)+" , "+Manifest.getMeta(context, "WX_APPSECRET", null));
        LoginShareLog.e(TAG, Manifest.getMeta(context, "SINA_APPKEY", null)+" , "+Manifest.getMeta(context, "SINA_APPSECRET", null)+" , "+Manifest.getMeta(context, "SINA_URL", null));


        String wxAppKey = Manifest.getMeta(context, "WX_APPKEY", null);
        String wxAppSecret = Manifest.getMeta(context, "WX_APPSECRET", null);
        if (null != wxAppKey && null != wxAppSecret) {

            PlatformConfig.setWeixin(wxAppKey, wxAppSecret);
        }

        String qqAppKey = Manifest.getMeta(context, "QQ_APPKEY", null);
        String qqAppSecret = Manifest.getMeta(context, "QQ_APPSECRET", null);
        if (null != qqAppKey && null != qqAppSecret) {

            PlatformConfig.setQQZone(qqAppKey.replace("tencent", ""), qqAppSecret);
        }


        String sinaAppKey = Manifest.getMeta(context, "SINA_APPKEY", null);
        String sinaAppSecret = Manifest.getMeta(context, "SINA_APPSECRET", null);
        String sinaUrl = Manifest.getMeta(context, "SINA_URL", null);
        if (null != sinaAppKey && null != sinaAppSecret) {

            PlatformConfig.setSinaWeibo(sinaAppKey.replace("sina", ""), sinaAppSecret, sinaUrl);
        }

        String umengAppKey = Manifest.getMeta(context, "UMENG_APPKEY", null);
        UMShareAPI.init(context, umengAppKey);
    }


    public static void fuckInit(Context context) {
        UMConfigure.setLogEnabled(true);
        String umengAppKey = Manifest.getMeta(context, "UMENG_APPKEY", null);
        UMConfigure.init(context, umengAppKey, "", UMConfigure.DEVICE_TYPE_PHONE,"");
    }
}
