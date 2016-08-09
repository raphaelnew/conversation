package ru.irafa.conversation.search;

/**
 * Base Search Provider class. Extend this class to provide search functionality for specific model.
 * Created by Raphael Gilyazitdinov on 07.08.16.
 */

abstract class BaseSearchProvider<M> {

    BaseSearchProvider() {
        initSearchProvider();
    }

    /**
     * Initialize search providers variables that should be persistant across lifecircle.
     */
    protected abstract void initSearchProvider();

    /**
     * Implement model related search logic, return results as {@link SearchResult}.
     */
    public abstract SearchResult<M> provideResult(String query);

}
