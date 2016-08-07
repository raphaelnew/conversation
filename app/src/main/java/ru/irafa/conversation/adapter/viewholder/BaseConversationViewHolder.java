package ru.irafa.conversation.adapter.viewholder;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.irafa.conversation.model.Message;

/**
 * Base ViewHolder for all types of Conversation messages.
 * Created by Raphael Gilyazitdinov on 06.08.16.
 */

abstract public class BaseConversationViewHolder extends RecyclerView.ViewHolder {

    Context mContext;

    public BaseConversationViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
    }

    public abstract void bind(Message message);

    public abstract ViewDataBinding getBinding();

}
