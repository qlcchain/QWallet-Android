package com.stratagile.qlink.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class QrEntity implements Parcelable {
    private String content;

    public QrEntity(String content, String title, String icon) {
        this.content = content;
        this.title = title;
        this.icon = icon;
    }

    protected QrEntity(Parcel in) {
        content = in.readString();
        title = in.readString();
        icon = in.readString();
    }

    public static final Creator<QrEntity> CREATOR = new Creator<QrEntity>() {
        @Override
        public QrEntity createFromParcel(Parcel in) {
            return new QrEntity(in);
        }

        @Override
        public QrEntity[] newArray(int size) {
            return new QrEntity[size];
        }
    };

    public String getContent() {
        return content;

    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private String title;
    private String icon;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(title);
        dest.writeString(icon);
    }
}
