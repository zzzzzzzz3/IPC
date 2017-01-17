package com.example.msi.ipc.modle;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 文 件 名: User
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/17 16:47
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class User implements Parcelable ,Serializable{
    private String name;
    private int id;
    private int sex;

    public User(String name,int id,int sex){
        this.name = name;
        this.id = id;
        this.sex = sex;
    }

    protected User(Parcel in) {
        name = in.readString();
        id = in.readInt();
        sex = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(id);
        parcel.writeInt(sex);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
