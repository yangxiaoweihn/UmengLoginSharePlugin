package ws.dyt.plugin.umeng.loginshare.main;

import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;

import ws.dyt.plugin.umeng.loginshare.utils.LoginShareLog;

/**
 * Created by yangxiaowei 2018/05/27
 *
 * 平台支持检测
 */
public class PlatSupportCheck {

    public static boolean checkPlatSupport(String plat, SHARE_MEDIA[] medias) {
        if (TextUtils.isEmpty(plat) || medias == null || medias.length == 0) {
            return false;
        }

        plat = plat.toLowerCase();
        boolean support = false;
        for (SHARE_MEDIA item : medias) {
            LoginShareLog.d("PlatSupportCheck", "check: "+item.getName().toLowerCase()+" , "+plat);
            if (item.getName().toLowerCase().equals(plat)) {
                support = true;
            }
        }

        return support;
    }


    public static boolean checkPlatSupportForLogin(String plat) {

        SHARE_MEDIA[] array = new SHARE_MEDIA[]{
                SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.SINA
        };

        return PlatSupportCheck.checkPlatSupport(plat, array);
    }

    public static boolean checkPlatSupportForShare(String plat) {

        SHARE_MEDIA[] array = new SHARE_MEDIA[]{
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.SINA,
        };

        return PlatSupportCheck.checkPlatSupport(plat, array);
    }
}
