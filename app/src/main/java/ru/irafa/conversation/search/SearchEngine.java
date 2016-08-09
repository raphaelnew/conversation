package ru.irafa.conversation.search;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Search engine provides high level methods for search functionality, delivers results to provided
 * {@link OnSearchListener}, model related search logic and results fetching provided by instance
 * of
 * {@link BaseSearchProvider}.
 * Created by Raphael Gilyazitdinov on 07.08.16.
 */

public class SearchEngine<M, S extends BaseSearchProvider<M>> {

    public interface OnSearchListener<M> {

        void onSearchCompleted(boolean success, @Nullable SearchResult<M> result);
    }

    private OnSearchListener<M> onSearchListener;

    private S searchProvider;

    public SearchEngine(@NonNull S searchProvider, @NonNull OnSearchListener<M> onSearchListener) {
        this.searchProvider = searchProvider;
        this.onSearchListener = onSearchListener;
    }

    /**
     * Starts search, calls {@link OnSearchListener#onSearchCompleted(boolean, SearchResult)} with
     * results.
     *
     * @param query search query.
     */
    public void search(String query) {
        SearchResult<M> result = searchProvider.provideResult(query);
        if (result.getResults() != null && !result.getResults().isEmpty()) {
            onSearchListener.onSearchCompleted(true, result);
        } else {
            onSearchListener.onSearchCompleted(false, null);
        }
    }
}