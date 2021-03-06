package ru.irafa.conversation.search;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Model that holds array of search results and original FTS query part.
 * Created by Raphael Gilyazitdinov on 07.08.16.
 */

public class SearchResult<M> {

    public SearchResult() {
    }

    private List<M> results = Collections.emptyList();

    private String fullTextSearchQuery;

    public void setResults(List<M> results) {
        this.results = results;
    }

    public void setFullTextSearchQuery(String fullTextSearchQuery) {
        this.fullTextSearchQuery = fullTextSearchQuery;
    }

    @NonNull
    public List<M> getResults() {
        return results;
    }

    @Nullable
    public String getFullTextSearchQuery() {
        return fullTextSearchQuery;
    }
}
