package ru.irafa.conversation.adapter.viewholder;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

import ru.irafa.conversation.adapter.ConversationSearchableAdapter;
import ru.irafa.conversation.databinding.ItemConversationLeftBinding;
import ru.irafa.conversation.model.Message;
import ru.irafa.conversation.util.CircleTransform;
import ru.irafa.conversation.util.Utils;

/**
 * ViewHolder implementation for showing message in conversation with view type
 * {@link ConversationSearchableAdapter#VIEW_TYPE_OTHER}.
 * Created by Raphael Gilyazitdinov on 06.08.16.
 */

public class SearchableViewHolderOther extends BaseSearchableViewHolder<Message> {

    private final ItemConversationLeftBinding mBinding;

    public SearchableViewHolderOther(Context context, ItemConversationLeftBinding binding) {
        super(context, binding.getRoot());
        mBinding = binding;
    }

    @Override
    public void bind(Message data, boolean foundInSearch, @Nullable String searchQuery) {
        mBinding.name.setText(data.getUser().getName());
        mBinding.content.setText(data.getContent());
        mBinding.date.setText(Utils.getReadableMessageDate(data.getPostedTs()));
        Picasso.with(mContext).load(data.getUser().getAvatarUrl())
                .transform(new CircleTransform()).into(mBinding.avatar);
        if (foundInSearch) {
            Utils.highLightText(mBinding.content, searchQuery);
            if (!mBinding.messageItem.isActivated()) {
                mBinding.messageItem.setActivated(true);
            }
        } else {
            mBinding.messageItem.setActivated(false);
        }
    }

    @Override
    public ViewDataBinding getBinding() {
        return mBinding;
    }

}
