package ru.irafa.conversation.search;

import java.util.List;

import ru.irafa.conversation.dao.DaoSession;
import ru.irafa.conversation.dao.MessageDao;
import ru.irafa.conversation.model.Message;

/**
 * {@link BaseSearchProvider} implementation provides search results for
 * {@link ru.irafa.conversation.model.Message} model.
 * Created by Raphael Gilyazitdinov on 07.08.16.
 */

public class MessagesSearchProvider extends BaseSearchProvider<Message> {

    private final String SEARCH_DATE_FORMAT = "yyyy-MM-dd";

    private final String SEARCH_TAG_BEFORE = "before:";

    private final String SEARCH_TAG_AFTER = "after:";

    private DaoSession daoSession;

    public MessagesSearchProvider(DaoSession daoSession) {
        super();
        this.daoSession = daoSession;
    }

    @Override
    public void initSearchProvider() {
        //// TODO: 07.08.16 initialize variables that shoud be persistent between searches.
    }

    @Override
    public SearchResult<Message> provideResult(String query) {
        if (query == null || query.isEmpty())return null;
        // Make SQLite query with 'LIKE' operator which is used to match text values against a pattern
        // using wildcards. GreenDao has great implementation for that operator.
        List<Message> results =daoSession.getMessageDao().queryBuilder()
                .orderAsc(MessageDao.Properties.PostedTs)
                .where(MessageDao.Properties.Content.like("%"+query+"%")).build().list();
        //check if we we found results.
        if (results!=null && !results.isEmpty()) {
            SearchResult<Message> searchResult= new SearchResult<>();
            searchResult.setFullTextSearchQuery(query);
            searchResult.setResults(results);
            return searchResult;
        }
        return null;
    }
}
