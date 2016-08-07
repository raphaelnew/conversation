package ru.irafa.conversation.adapter.viewholder;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.databinding.ViewDataBinding;

import ru.irafa.conversation.databinding.ItemConversationLeftBinding;
import ru.irafa.conversation.model.Message;
import ru.irafa.conversation.util.CircleTransform;
import ru.irafa.conversation.util.TimeDateUtils;

/**
 * ViewHolder implementation for showing message in conversation with view type
 * {@link ru.irafa.conversation.adapter.ConversationAdapter#VIEW_TYPE_OTHER}.
 * Created by Raphael Gilyazitdinov on 06.08.16.
 */

public class ConversationViewHolderOther extends BaseConversationViewHolder {

    private final ItemConversationLeftBinding mBinding;

    public ConversationViewHolderOther(Context context, ItemConversationLeftBinding binding) {
        super(context, binding.getRoot());
        mBinding = binding;
    }

    @Override
    public void bind(Message message) {
        mBinding.name.setText(message.getUser().getName());
        mBinding.content.setText(message.getContent());
        mBinding.date.setText(TimeDateUtils.getReadableMessageDate(message.getPostedTs()));
        Picasso.with(mContext).load(message.getUser().getAvatarUrl())
                .transform(new CircleTransform()).into(mBinding.avatar);
    }

    @Override
    public ViewDataBinding getBinding() {
        return mBinding;
    }

}
