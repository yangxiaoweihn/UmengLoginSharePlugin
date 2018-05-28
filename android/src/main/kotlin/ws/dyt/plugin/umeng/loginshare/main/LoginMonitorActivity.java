package ws.dyt.plugin.umeng.loginshare.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import ws.dyt.plugin.umeng.loginshare.utils.LoginShareLog;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 三方登录回调处理
 */
public class LoginMonitorActivity extends Activity implements UMAuthListener{
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String plat = getIntent().getStringExtra("plat").toUpperCase();
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.convertToEmun(plat), this);
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        LoginShareLog.d("DEBUG", "auth -> start");
    }

    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
        LoginShareLog.d(TAG, "auth -> complete");

        JSONObject object = new JSONObject();
        try {

            object.put("uid", map.get("uid"));
            object.put("nickName", map.get("name"));
            String gender = map.get("gender");
            object.put("gender", gender.equals("男") ? 1 : gender.equals("女") ? 0 : -1);
            object.put("avatar", map.get("iconurl"));
            object.put("expiration", map.get("expiration"));
            object.put("accessToken", map.get("accessToken"));
        }catch (JSONException e) {
            e.printStackTrace();
        }
        String json = object.toString();
        LoginShareLog.d(TAG, "json: "+json);
        this.onComplete(share_media, json);
    }

    protected void onComplete(SHARE_MEDIA share_media, String json) {}

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        LoginShareLog.d(TAG, "auth -> error ： "+throwable.getMessage());
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        LoginShareLog.d(TAG, "auth -> cancel");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

}
