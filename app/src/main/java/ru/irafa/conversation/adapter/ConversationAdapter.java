package ru.irafa.conversation.adapter;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.List;

import ru.irafa.conversation.adapter.viewholder.BaseConversationViewHolder;
import ru.irafa.conversation.adapter.viewholder.ConversationViewHolderOther;
import ru.irafa.conversation.adapter.viewholder.ConversationViewHolderSelf;
import ru.irafa.conversation.databinding.ItemConversationLeftBinding;
import ru.irafa.conversation.databinding.ItemConversationRightBinding;
import ru.irafa.conversation.model.Message;

/**
 * Implementation of {@link android.support.v7.widget.RecyclerView.Adapter} for showing
 * Conversation messages in {@link RecyclerView}.
 * Created by Raphael Gilyazitdinov on 06.08.16.
 */

public class ConversationAdapter extends RecyclerView.Adapter<BaseConversationViewHolder> {

    @IntDef({VIEW_TYPE_SELF, VIEW_TYPE_OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewType {

    }

    public static final int VIEW_TYPE_SELF = 0;

    public static final int VIEW_TYPE_OTHER = 1;

    private List<Message> mDataset = Collections.emptyList();

    private final Context mContext;

    private long selfId = -1L;

    public ConversationAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public BaseConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_OTHER:
                ItemConversationLeftBinding leftBinding = ItemConversationLeftBinding
                        .inflate(LayoutInflater.from(mContext), parent, false);
                return new ConversationViewHolderOther(mContext, leftBinding);
            case VIEW_TYPE_SELF:
            default:
                ItemConversationRightBinding rightBinding = ItemConversationRightBinding
                        .inflate(LayoutInflater.from(mContext), parent, false);
                return new ConversationViewHolderSelf(mContext, rightBinding);
        }
    }

    @Override
    public void onBindViewHolder(BaseConversationViewHolder holder, int position) {
        Message message = mDataset.get(position);
        // We provide object for binding if we use 2-way data-binding etc. Not in this project.
        // holder.getBinding().setVariable(BR.item, item);
        holder.bind(message);
        // trigger queued bindings early (as opposed to next animation frame).
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemViewType(int position) {
        return (mDataset.get(position).getUserId() == selfId) ? VIEW_TYPE_SELF : VIEW_TYPE_OTHER;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * Update dataset in the adapter.
     */
    public void updateItems(List<Message> dataset) {
        // In the test project we don't have any information about who are users, so
        // we get userId from first message and tell Adapter to show this user messages as ours, using
        // specific viewType. In production scenario constructor can have additional field where we
        // provide User model/userId of signed user on this device.
        if (selfId < 0L && dataset != null && !dataset.isEmpty()) {
            selfId = dataset.get(0).getUserId();
        }

        mDataset = dataset;
        notifyDataSetChanged();
    }
}
