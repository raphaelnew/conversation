package ru.irafa.conversation.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.irafa.conversation.adapter.viewholder.BaseSearchableViewHolder;
import ru.irafa.conversation.search.SearchResult;

/**
 * Base Adapter class.
 * Created by Raphael Gilyazitdinov on 07.08.16.
 */

public abstract class BaseSearchableAdapter<M, VH extends BaseSearchableViewHolder<M>>
        extends RecyclerView.Adapter<VH> {

    private List<M> mDataset = new ArrayList<M>();

    private SearchResult<M> mSearchResult;

    Context mContext;

    public BaseSearchableAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        M data = mDataset.get(position);
        // We provide object for binding if we use 2-way data-binding etc. Not in this project.
        // holder.getBinding().setVariable(BR.item, item);
        if (mSearchResult == null || !mSearchResult.getResults().contains(data)) {
            holder.bind(data, false, null);
        } else {
            holder.bind(data, true, mSearchResult.getFullTextSearchQuery());
        }
        // trigger queued bindings early (as opposed to next animation frame).
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * Return dada set from the adapter.
     *
     * @return data set.
     */
    public List<M> getDataset() {
        return mDataset;
    }

    /**
     * Update data set in the adapter.
     */
    public void updateDataSet(List<M> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }

    /**
     * Apples search results, notifies all ViewHolders during
     * {@code {@link BaseSearchableAdapter#onBindViewHolder(BaseSearchableViewHolder, int)}).
     */
    public void applySearchResult(@Nullable SearchResult<M> searchResult) {
        mSearchResult = searchResult;
        notifyDataSetChanged();
    }

    /**
     * Return position of the data item in the adapter dataset.
     *
     * @return position of the data item, 0 otherwise.
     */
    public int getDataItemPosition(M dataItem) {
        if (!mDataset.contains(dataItem)) {
            return 0;
        }
        return mDataset.indexOf(dataItem);
    }
}
