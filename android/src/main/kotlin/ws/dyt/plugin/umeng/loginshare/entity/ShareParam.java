package ws.dyt.plugin.umeng.loginshare.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class ShareParam implements Parcelable {
    public String plat;
    public String type;
    public String content;
    public String url;
    public String thumbUrl;
    public String title;
    public String description;

    public ShareParam(String plat, String type) {
        this.plat = plat;
        this.type = type;
    }

    public boolean validate() {
        return !TextUtils.isEmpty(plat) && !TextUtils.isEmpty(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.plat);
        dest.writeString(this.type);
        dest.writeString(this.content);
        dest.writeString(this.url);
        dest.writeString(this.thumbUrl);
        dest.writeString(this.title);
        dest.writeString(this.description);
    }

    protected ShareParam(Parcel in) {
        this.plat = in.readString();
        this.type = in.readString();
        this.content = in.readString();
        this.url = in.readString();
        this.thumbUrl = in.readString();
        this.title = in.readString();
        this.description = in.readString();
    }

    public static final Creator<ShareParam> CREATOR = new Creator<ShareParam>() {
        @Override
        public ShareParam createFromParcel(Parcel source) {
            return new ShareParam(source);
        }

        @Override
        public ShareParam[] newArray(int size) {
            return new ShareParam[size];
        }
    };
}