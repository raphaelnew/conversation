package ru.irafa.conversation.adapter;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import ru.irafa.conversation.adapter.viewholder.BaseSearchableViewHolder;
import ru.irafa.conversation.adapter.viewholder.SearchableViewHolderOther;
import ru.irafa.conversation.adapter.viewholder.SearchableViewHolderSelf;
import ru.irafa.conversation.databinding.ItemConversationLeftBinding;
import ru.irafa.conversation.databinding.ItemConversationRightBinding;
import ru.irafa.conversation.model.Message;

/**
 * Implementation of {@link android.support.v7.widget.RecyclerView.Adapter} for showing
 * Conversation messages in {@link RecyclerView}.
 * Created by Raphael Gilyazitdinov on 06.08.16.
 */

public class ConversationSearchableAdapter
        extends BaseSearchableAdapter<Message, BaseSearchableViewHolder<Message>> {

    @IntDef({VIEW_TYPE_SELF, VIEW_TYPE_OTHER})
    @Retention(RetentionPolicy.SOURCE)
    private @interface ViewType {

    }

    private static final int VIEW_TYPE_SELF = 0;

    private static final int VIEW_TYPE_OTHER = 1;

    private long mSelfId = -1L;

    public ConversationSearchableAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseSearchableViewHolder<Message> onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_OTHER:
                ItemConversationLeftBinding leftBinding = ItemConversationLeftBinding
                        .inflate(LayoutInflater.from(mContext), parent, false);
                return new SearchableViewHolderOther(mContext, leftBinding);
            case VIEW_TYPE_SELF:
            default:
                ItemConversationRightBinding rightBinding = ItemConversationRightBinding
                        .inflate(LayoutInflater.from(mContext), parent, false);
                return new SearchableViewHolderSelf(mContext, rightBinding);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (getDataset().get(position).getUserId() == mSelfId) ? VIEW_TYPE_SELF
                : VIEW_TYPE_OTHER;
    }

    @Override
    public void updateDataSet(List<Message> dataset) {
        // In the test project we don't have any information about who are users, so
        // we get userId from first message and tell Adapter to show this user messages as ours, using
        // specific viewType. In production scenario constructor can have additional field where we
        // provide User model/userId of signed user on this device.
        if (mSelfId < 0L && dataset != null && !dataset.isEmpty()) {
            mSelfId = dataset.get(0).getUserId();
        }
        super.updateDataSet(dataset);
    }

}
