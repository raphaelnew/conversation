package ru.irafa.conversation.model;

//import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import android.os.Parcel;
import android.os.Parcelable;

import ru.irafa.conversation.dao.DaoSession;
import ru.irafa.conversation.dao.MessageDao;
import ru.irafa.conversation.dao.UserDao;

/**
 * POJO/DB model class for Message in conversation. Implements Parcelable to be able to pass object
 * through Intent extras etc.
 * original Author: Gonçalo Silva, edited by: Raphael Gilyazitdinov.
 * original source: https://gist.github.com/goncalossilva/ce557f75d3fe3079531d97ceb068dcc5
 */

@Entity(
        // Specifies the name of the table in the database.
        // By default, the name is based on the entities class name.
        nameInDb = "message"
)
public class Message implements Parcelable{

    @Unique
    @Id(autoincrement = false)
    private long id;

    @SerializedName("user_id")
    private Long userId;

    @ToOne(joinProperty = "userId")
    private User user;

    @Property
    @SerializedName("posted_ts")
    private Long postedTs;

    @Property
    private String content;

    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;

    /** Used for active entity operations. */
    @Generated(hash = 859287859)
    private transient MessageDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    @Generated(hash = 637306882)
    public Message() {
    }

    protected Message(Parcel in) {
        id = in.readLong();
        userId = in.readLong();
        user = in.readParcelable(User.class.getClassLoader());
        postedTs = in.readLong();
        content = in.readString();
    }

    @Generated(hash = 1922876363)
    public Message(long id, Long userId, Long postedTs, String content) {
        this.id = id;
        this.userId = userId;
        this.postedTs = postedTs;
        this.content = content;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getPostedTs() {
        return postedTs;
    }

    public String getContent() {
        return content;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1065606912)
    public void setUser(User user) {
        synchronized (this) {
            this.user = user;
            userId = user == null ? null : user.getId();
            user__resolvedKey = userId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 859885876)
    public User getUser() {
        Long __key = this.userId;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
                user__resolvedKey = __key;
            }
        }
        return user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", userId=" + userId +
                ", user=" + ((getUser() == null) ? "" : getUser().toString()) +
                ", postedTs=" + postedTs +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(userId);
        parcel.writeParcelable(user, i);
        parcel.writeLong(postedTs);
        parcel.writeString(content);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 747015224)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMessageDao() : null;
    }

    public void setPostedTs(Long postedTs) {
        this.postedTs = postedTs;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
