package ru.irafa.conversation.model;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO/DB model class for Author of the conversation message. Implements Parcelable to be able to
 * pass object through Intent extras etc.
 * original Author: Gonçalo Silva, edited by: Raphael Gilyazitdinov.
 * original source: https://gist.github.com/goncalossilva/ce557f75d3fe3079531d97ceb068dcc5
 */

@Entity(
        // Specifies the name of the table in the database.
        // By default, the name is based on the entities class name.
        nameInDb = "user"
)
public class User implements Parcelable {

    @Unique
    @Id(autoincrement = false)
    private long id;

    @Property
    private String name;

    @SerializedName("avatar_url")
    @Property
    private String avatarUrl;

    @Generated(hash = 586692638)
    public User() {
    }

    protected User(Parcel in) {
        id = in.readLong();
        name = in.readString();
        avatarUrl = in.readString();
    }

    @Generated(hash = 872006314)
    public User(long id, String name, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
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

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(avatarUrl);
    }
}
