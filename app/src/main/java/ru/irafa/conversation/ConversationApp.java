package ru.irafa.conversation;

import org.greenrobot.greendao.database.Database;

import android.app.Application;

import ru.irafa.conversation.dao.DaoMaster;
import ru.irafa.conversation.dao.DaoSession;

/**
 * Created by Raphael Gilyazitdinov on 06.08.16.
 */

public class ConversationApp extends Application {

    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "conversation-db");
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
