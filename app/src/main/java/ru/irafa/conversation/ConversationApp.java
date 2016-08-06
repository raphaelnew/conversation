package ru.irafa.conversation;

import org.greenrobot.greendao.database.Database;

import android.app.Application;
import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.irafa.conversation.api.ConversationApi;
import ru.irafa.conversation.dao.DaoMaster;
import ru.irafa.conversation.dao.DaoSession;

/**
 * Created by Raphael Gilyazitdinov on 06.08.16.
 */

public class ConversationApp extends Application {

    private static ConversationApi mConversationApi;

    private static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        getDaoSession(this);
    }

    public static DaoSession getDaoSession(Context context) {
        if (mDaoSession == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,
                    "conversation-db");
            Database db = helper.getWritableDb();
            mDaoSession = new DaoMaster(db).newSession();
        }
        return mDaoSession;
    }

    public static ConversationApi getConversationApi() {
        if (mConversationApi == null) {
            OkHttpClient client;
            //Only use HttpLoggingInterceptor while Debug build type selected. Dependency doesn't
            //compile in Release mode for security reasons.
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                        : HttpLoggingInterceptor.Level.NONE);
                client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            } else {
                client = new OkHttpClient.Builder().build();
            }
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_ENDPOINT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mConversationApi = retrofit.create(ConversationApi.class);
        }
        return mConversationApi;
    }
}
