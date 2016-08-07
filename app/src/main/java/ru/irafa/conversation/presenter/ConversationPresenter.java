package ru.irafa.conversation.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.irafa.conversation.ConversationApp;
import ru.irafa.conversation.dao.DaoSession;
import ru.irafa.conversation.dao.MessageDao;
import ru.irafa.conversation.model.Conversation;
import ru.irafa.conversation.model.Message;

/**
 * Presenter provides messages from conversation.
 * Created by Raphael Gilyazitdinov on 06.08.16.
 */

public class ConversationPresenter {

    public interface OnConversationListener {

        void onConversationLoading();

        void onConversationEmpty();

        void onConversationLoadingError(String message);

        void onConversationChanged(@NonNull List<Message> messages);
    }

    private DaoSession daoSession;

    private OnConversationListener onConversationListener;

    public static ConversationPresenter fromSavedState(DaoSession daoSession,
            @NonNull OnConversationListener onConversationListener,
            @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //// TODO: 06.08.16 restore state after device configuration changes.
        }
        return new ConversationPresenter(daoSession, onConversationListener, false);
    }

    public ConversationPresenter(DaoSession daoSession,
            @NonNull OnConversationListener onConversationListener) {
        this(daoSession, onConversationListener, true);
    }

    public ConversationPresenter(DaoSession daoSession,
            @NonNull OnConversationListener onConversationListener,
            boolean sync) {
        this.onConversationListener = onConversationListener;
        this.daoSession = daoSession;

        boolean gotCachedConversation = sendResultEvent(false);
        if (sync || !gotCachedConversation) {
            asyncRequest();
        }
    }

    /**
     * Does request to refresh Conversation data from API
     */
    public void refresh() {
        asyncRequest();
    }

    /**
     * Call this method from {@link Fragment#onDestroyView()} or from {@link
     * AppCompatActivity#onDestroy()}.
     */
    public void destroy() {
        onConversationListener = null;
        ConversationApp.getConversationApi().getConversation().cancel();
    }

    public void onSaveInstanceState(Bundle outState) {
        //// TODO: 06.08.16 save state between device configuration changes.
    }

    /**
     * Asynchronous request to API.
     */
    private void asyncRequest() {
        if (onConversationListener != null) {
            onConversationListener.onConversationLoading();
        }
        ConversationApp.getConversationApi().getConversation().cancel();
        ConversationApp.getConversationApi().getConversation().enqueue(
                new Callback<Conversation>() {
                    @Override
                    public void onResponse(Call<Conversation> call,
                            Response<Conversation> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            processResult(response.body());
                        } else {
                            processError(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Conversation> call, Throwable t) {
                        processError(t.getLocalizedMessage());
                    }
                });
    }

    /**
     * Saves result from API request to DB, sends result event.
     *
     * @param conversation POJO model from API request.
     */
    private void processResult(Conversation conversation) {
        conversation.saveToDB(daoSession);
        sendResultEvent(true);
    }

    /**
     * Sends {@link OnConversationListener#onConversationChanged(List)} event if at least one
     * message from conversation available in DB, otherwise sends
     * {@link OnConversationListener#onConversationEmpty()}
     *
     * @param errorIfEmpty if set {@code true} will send {@link OnConversationListener#onConversationEmpty()}
     *                     if no messages from conversation available in DB.
     * @return {@code true} at least one message from Conversation available in DB, {@code false}
     * otherwise.
     */
    private boolean sendResultEvent(boolean errorIfEmpty) {
        if (onConversationListener == null) {
            return false;
        }
        long messageCount = daoSession.getMessageDao().queryBuilder().count();
        List<Message> results =daoSession.getMessageDao().queryBuilder()
                .orderAsc(MessageDao.Properties.PostedTs).build().list();
        //check if we already have conversation in DB.
        if (messageCount > 0L && results!=null && !results.isEmpty()) {
            onConversationListener.onConversationChanged(
                    results);
            return true;
        } else if (errorIfEmpty) {
            processError(null);
        }
        return false;
    }

    /**
     * Sends {@link OnConversationListener#onConversationLoadingError(String)} event with error
     * message.
     *
     * @param message with error.
     */
    private void processError(String message) {
        if (onConversationListener == null) {
            return;
        }
        if (message != null) {
            onConversationListener.onConversationLoadingError(message);
        } else {
            onConversationListener.onConversationEmpty();
        }
    }
}
