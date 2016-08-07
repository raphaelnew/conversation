package ru.irafa.conversation.search;

/**
 * Base Search Provider class.
 * Created by Raphael Gilyazitdinov on 07.08.16.
 */

abstract public class BaseSearchProvider<M> {

    public BaseSearchProvider() {
        initSearchProvider();
    }

    /**
     * Initialize search providers variables that should be persistant across lifecircle.
     */
    public abstract void initSearchProvider();

    /**
     * Implement model related search logic, return results as {@link SearchResult}.
     */
    public abstract SearchResult<M> provideResult(String query);

}
