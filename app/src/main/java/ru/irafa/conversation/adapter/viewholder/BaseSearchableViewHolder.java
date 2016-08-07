package ru.irafa.conversation.adapter.viewholder;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Base ViewHolder class.
 * Created by Raphael Gilyazitdinov on 06.08.16.
 */

abstract public class BaseSearchableViewHolder<M> extends RecyclerView.ViewHolder {

    Context mContext;

    public BaseSearchableViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
    }

    public abstract void bind(M data, boolean foundInSearch, @Nullable String searchQuery);

    public abstract ViewDataBinding getBinding();
}
