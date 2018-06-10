package ws.dyt.plugin.umeng.loginshare.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import ws.dyt.plugin.umeng.loginshare.R;
import ws.dyt.plugin.umeng.loginshare.utils.LoginShareLog;

/**
 * Created by yangxiaowei on 2018/2/15.
 *
 * 分享、登录 动作监听分发
 *
 * 分享、登录 广播接受到后启动分享监听activity{@link ShareMonitorActivity} {@link LoginMonitorActivity}
 */

public class ActionDispatchMonitorReceiver extends BroadcastReceiver{
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "ActionDispatchMonitorReceiver -> action: "+intent.getAction());

        final String action = intent.getAction();

        //调用者可以设置回调activity全名
        String ac = intent.getStringExtra("callback_activity");
        Class targetClazz = null;
        try {

            if (action.equals(context.getString(R.string.action_umeng_share_data))) {

                targetClazz = TextUtils.isEmpty(ac) ? ShareMonitorActivity.class : Class.forName(ac);
            }else if (action.equals(context.getString(R.string.action_umeng_login_data))) {

                targetClazz = TextUtils.isEmpty(ac) ? LoginMonitorActivity.class : Class.forName(ac);
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (null != targetClazz) {

            //过来的数据全部都不变-传到要启动的activity中
            intent.setClass(context, targetClazz);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.dispatchShare(context, intent);
        }
    }

    private void dispatchShare(Context context, Intent intent) {

        LoginShareLog.d(TAG, "dispatchShare -> "+(intent.getComponent() == null ? null : intent.getComponent().getClassName()));

        context.startActivity(intent);
    }
}
