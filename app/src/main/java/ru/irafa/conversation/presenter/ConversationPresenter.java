package ru.irafa.conversation.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.irafa.conversation.model.Message;

/**
 * Presenter provides messages from conversation.
 * Created by Raphael Gilyazitdinov on 06.08.16.
 */

public class ConversationPresenter {

    public interface OnConversationListener {

        void onConversationLoading();

        void onConversationLoadingError(String message, boolean canRetry);

        void onConversationChanged(List<Message> messages);
    }

    private OnConversationListener onConversationListener;

    public static ConversationPresenter fromSavedState(@Nullable Bundle savedInstanceState, @NonNull OnConversationListener onConversationListener){
        if(savedInstanceState!=null){
            //// TODO: 06.08.16 restore state after device configuration changes.
        }
        return new ConversationPresenter(onConversationListener, false);
    }

    public ConversationPresenter(@NonNull OnConversationListener onConversationListener) {
        this(onConversationListener, true);
    }

    public ConversationPresenter(@NonNull OnConversationListener onConversationListener,
            boolean sync) {
        this.onConversationListener = onConversationListener;
        //// TODO: 06.08.16 initialize presenter. check DB for existing conversation. sync if needed.
    }

    public void sync() {
        //// TODO: 06.08.16 sync conversation in DB with backend API.
    }

    public void destroy() {
        //// TODO: 06.08.16 clean everything. stop any network requests etc.
    }

    public void onSaveInstanceState(Bundle outState) {
        //// TODO: 06.08.16 save state between device configuration changes.
    }
}
