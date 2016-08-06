package ru.irafa.conversation.api;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.irafa.conversation.model.Conversation;

/**
 * API interface.
 * Created by Raphael Gilyazitdinov on 06.08.16.
 */

public interface ConversationApi {

    @GET("/u/1585962/api.json")
    Call<Conversation> getConversation();
}
