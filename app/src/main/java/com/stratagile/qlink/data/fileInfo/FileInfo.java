package com.stratagile.qlink.data.fileInfo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;


import java.io.File;

import io.julian.common.Preconditions;


/**
 * @author Zhu Liang
 * @version 1.0
 * @since 2017/5/13 下午2:34
 */

public class FileInfo implements Parcelable, Comparable<FileInfo> {

    @NonNull
    private File mFile;

    @NonNull
    public File getmFile() {
        return mFile;
    }

    public void setmFile(@NonNull File mFile) {
        this.mFile = mFile;
    }

    public FileInfo(String pathname) {
        this(new File(pathname));
    }

    public FileInfo(@NonNull File file) {
        mFile = Preconditions.checkNotNull(file);
    }

    public boolean isDirectory() {
        return mFile.isDirectory();
    }

    public boolean isFile() {
        return mFile.isFile();
    }

    public String getName() {
        return mFile.getName();
    }

    public String getAbsolutePath() {
        return mFile.getAbsolutePath();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileInfo info = (FileInfo) o;

        return mFile.equals(info.mFile);

    }

    @Override
    public int hashCode() {
        return mFile.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.mFile);
    }

    protected FileInfo(Parcel in) {
        this.mFile = (File) in.readSerializable();
    }

    public static final Creator<FileInfo> CREATOR = new Creator<FileInfo>() {
        @Override
        public FileInfo createFromParcel(Parcel source) {
            return new FileInfo(source);
        }

        @Override
        public FileInfo[] newArray(int size) {
            return new FileInfo[size];
        }
    };

    @Override
    public int compareTo(@NonNull FileInfo o) {
        int i;
        int oIsFile = o.isFile()? 1 : 0;
        int isFile = isFile()? 1: 0;
        i = isFile - oIsFile;
        if (i == 0) {
            int oUniCode = (int)o.getName().charAt(0);
            int uniCode = (int)getName().charAt(0);
            return uniCode - oUniCode;
        } else {
            return i;
        }

    }
}
