package ws.dyt.plugin.umeng.loginshare.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.Locale;

import ws.dyt.plugin.umeng.loginshare.R;
import ws.dyt.plugin.umeng.loginshare.entity.ShareParam;
import ws.dyt.plugin.umeng.loginshare.utils.LoginShareLog;

/**
 * 分享逻辑及结果监听
 *
 * 本模块间分享的数据都挂载在intent的data中,挂载数据为实体对象{@link ws.dyt.plugin.umeng.loginshare.entity.ShareParam}
 *
 */
public class ShareMonitorActivity extends Activity implements UMShareListener{
    private String TAG = getClass().getSimpleName();

    private String mContent;
    private String mThumbUrl;
    private String mUrl;
    private String mTitle;
    private String mDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.dispatchShare(this, getIntent());
    }

    private String mType;
    private SHARE_MEDIA mPlat;
    private void dispatchShare(Context context, Intent intent) {
        ShareParam param = null;
        if (intent.hasExtra("data")) {
            param = intent.getParcelableExtra("data");
        }

        //TODO 可以添加data为json类型的处理，可以避免调用者调用ShareParam引起模块代码污染
        if (null == param) {
            LoginShareLog.e(TAG, "The share `data` must not null.");
            finish();
            return;
        }

        String plat = param.plat;
        //text, image, web[link|music|video]
        String type = param.type;
        this.mType = type;
        this.mContent = param.content;
        this.mThumbUrl = param.thumbUrl;
        this.mUrl = param.url;
        this.mTitle = param.title;
        this.mDescription = param.description;

        if (TextUtils.isEmpty(plat) || TextUtils.isEmpty(type)) {
            LoginShareLog.e(TAG, "param missing");
            finish();
            return;
        }

        SHARE_MEDIA media = SHARE_MEDIA.convertToEmun(plat);
        this.mPlat = media;
        LoginShareLog.d(TAG, String.format(Locale.getDefault(), "plat: "+plat+" , media:  " + media));
        if (this.mPlat == null) {
            return;
        }

        if (!UMShareAPI.get(this).isInstall(this, media)) {
            Toast.makeText(this, R.string.tips_app_not_install, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (this.permissionsCheck()) {
            LoginShareLog.i(TAG, "permission granted.");
            this.doShareBy(media, type);
        }else {
            LoginShareLog.e(TAG, "permission not granted.");
        }
    }

    private static final int CODE_PERMISSION = 0x10001;
    private boolean permissionsCheck() {
        if(Build.VERSION.SDK_INT >= 23) {
            String[] permissionList = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.CALL_PHONE,
//                    Manifest.permission.READ_LOGS,
//                    Manifest.permission.READ_PHONE_STATE,
            };
            ActivityCompat.requestPermissions(this, permissionList, CODE_PERMISSION);

            return false;
        }

        return true;
    }

    private void doShareBy(SHARE_MEDIA plat, String type) {
        if (type.equals("image")) {

            this.shareImage(plat);
        }else if (type.equals("web")) {

            this.shareWeb(plat);
        }else {

            this.shareText(plat);
        }
    }

    private void shareText(SHARE_MEDIA plat){
        new ShareAction(this).withText(this.mContent)
                .setPlatform(plat)
                .setCallback(this).share();
    }

    /**
     * url: 原图片
     * thumbUrl: 缩略图
     */
    private void shareImage(SHARE_MEDIA plat){
        //图片大小最好不要超过250k，缩略图不要超过18k
        UMImage pic = new UMImage(this, mUrl);
        if (!TextUtils.isEmpty(mThumbUrl)) {
            pic.setThumb(new UMImage(this, mThumbUrl));
        }
        pic.setDescription(mDescription);
        new ShareAction(this)
                .setPlatform(plat)
                .withMedia(pic)
                .setCallback(this)
                .withText(mContent)
                .share();
    }

    /**
     * thumbUrl: 缩略图
     * url: web url
     */
    private void shareWeb(SHARE_MEDIA plat){
        UMWeb web = new UMWeb(this.mUrl);
        if (!TextUtils.isEmpty(mThumbUrl)) {
            UMImage thumb = new UMImage(this, this.mThumbUrl);
            web.setThumb(thumb);
        }
        web.setDescription(this.mDescription);
        web.setTitle(this.mTitle);
        new ShareAction(this)
                .setPlatform(plat)
                .withMedia(web)
                .setCallback(this)
                .share();
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {}

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        this.showToast(R.string.tips_share_rel_ok);
        LoginShareLog.d(TAG, "onResult");
        finish();
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        this.showToast(R.string.tips_share_rel_error);
        LoginShareLog.e(TAG, "onError: "+throwable.getMessage());
        finish();
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        this.showToast(R.string.tips_share_rel_cancel);
        LoginShareLog.d(TAG, "onCancel");
        finish();
    }

    private void showToast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CODE_PERMISSION) {

            boolean granted = true;
            if (null != grantResults && grantResults.length != 0) {
                for (int r:grantResults) {
                    granted &= (r == PackageManager.PERMISSION_GRANTED);
                }
            }else {
                granted = false;
            }

            if (granted) {
                this.doShareBy(this.mPlat, this.mType);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
