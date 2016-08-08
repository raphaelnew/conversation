package ru.irafa.conversation.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private SimpleDateFormat dateFormat;

    private String fullTextSearchQuery = null;

    private Long beforeTimestamp = -1L;

    private Long afterTimestamp = -1L;

    public MessagesSearchProvider(DaoSession daoSession) {
        super();
        this.daoSession = daoSession;
    }

    @Override
    public void initSearchProvider() {
        this.dateFormat = new SimpleDateFormat(SEARCH_DATE_FORMAT);
        this.dateFormat.setLenient(false);
    }

    @Override
    public SearchResult<Message> provideResult(String query) {
        if (query == null || query.isEmpty()) {
            return null;
        }
        query = query.trim();

        validateTagsAndQuery(query);

        List<Message> results = getSearchResultsFromDB();

        //check if we we found results.
        if (results != null && !results.isEmpty()) {
            SearchResult<Message> searchResult = new SearchResult<>();
            searchResult.setFullTextSearchQuery(fullTextSearchQuery);
            searchResult.setResults(results);
            return searchResult;
        }
        return null;
    }

    /**
     * Validates search query, finds supported tags, prepares for data fetching from DB.
     */
    private void validateTagsAndQuery(String query) {
        fullTextSearchQuery = null;
        beforeTimestamp = -1L;
        afterTimestamp = -1L;

        StringBuffer sb = new StringBuffer();
        sb = searchBeforeTag(query, sb);
        sb = searchAfterTag(query, sb);
        fullTextSearchQuery = sb.toString().trim();
    }

    private StringBuffer searchBeforeTag(String query, StringBuffer sb) {
        // Regex pattern (?i)\\s?\\bbefore:\\d{4}-[01]\\d-[0-3]\\d\\b\\s? - case-insensitive,
        // checks key word,
        // format 4 numbers for year, month can start from 0 or 1,
        // date can start with with numbers 0-3.
        // This pattern doesn't validate values only format, values will be validated later.
        Pattern beforePattern = Pattern
                .compile("(?i)\\s?\\b" + SEARCH_TAG_BEFORE + "\\d{4}-[01]\\d-[0-3]\\d\\b\\s?");
        Matcher beforeMatcher = beforePattern.matcher(query);

        // There might be multiple Tags of the same type, we only last one,
        // but we clean all of them.
        String beforeTag = null;
        while (beforeMatcher.find()) {
            beforeMatcher.appendReplacement(sb, " ");
            beforeTag = beforeMatcher.group().trim().replace(SEARCH_TAG_BEFORE, "");
        }
        beforeMatcher.appendTail(sb);
        // Second step we validate that date is correct, can be parsed by SimpleDateFormat, this
        // will eliminate situations with wrong days of the month like february 29 etc.
        if (beforeTag != null && !beforeTag.isEmpty()) {
            try {
                Date beforeDate = dateFormat.parse(beforeTag);
                beforeTimestamp = beforeDate.getTime() / 1000L;
            } catch (ParseException ex) {
            }
        }
        return sb;
    }

    private StringBuffer searchAfterTag(String query, StringBuffer sb) {
        // Regex pattern (?i)\\s?\\bafter:\\d{4}-[01]\\d-[0-3]\\d\\b\\s? - case-insensitive,
        // checks key word,
        // format 4 numbers for year, month can start from 0 or 1,
        // date can start with with numbers 0-3.
        // This pattern doesn't validate values only format, values will be validated later.
        Pattern afterPattern = Pattern
                .compile("(?i)\\s?\\b" + SEARCH_TAG_AFTER + "\\d{4}-[01]\\d-[0-3]\\d\\b\\s?");
        Matcher afterMatcher = afterPattern.matcher(sb.toString());
        sb = new StringBuffer();
        String afterTag = null;
        while (afterMatcher.find()) {
            afterMatcher.appendReplacement(sb, " ");
            afterTag = afterMatcher.group().trim().replace(SEARCH_TAG_AFTER, "");
        }
        afterMatcher.appendTail(sb);
        // Second step we validate that date is correct, can be parsed by SimpleDateFormat, this
        // will eliminate situations with wrong days of the month like february 29 etc.
        if (afterTag != null && !afterTag.isEmpty()) {
            try {
                Date afterDate = dateFormat.parse(afterTag);
                afterTimestamp = afterDate.getTime() / 1000L;
            } catch (ParseException ex) {
            }
        }
        return sb;
    }

    private List<Message> getSearchResultsFromDB() {
        List<Message> results = null;
        // Make SQLite query with 'LIKE' operator which is used to match text values against a pattern
        // using wildcards. GreenDao has great implementation for that operator.
        if (beforeTimestamp >= 0L && afterTimestamp >= 0L) {
            results = daoSession.getMessageDao().queryBuilder()
                    .orderAsc(MessageDao.Properties.PostedTs)
                    .where(MessageDao.Properties.PostedTs.lt(beforeTimestamp))
                    .where(MessageDao.Properties.PostedTs.gt(afterTimestamp))
                    .where(MessageDao.Properties.Content.like("%" + fullTextSearchQuery + "%"))
                    .build().list();
        } else if (beforeTimestamp >= 0L) {
            results = daoSession.getMessageDao().queryBuilder()
                    .orderAsc(MessageDao.Properties.PostedTs)
                    .where(MessageDao.Properties.PostedTs.lt(beforeTimestamp))
                    .where(MessageDao.Properties.Content.like("%" + fullTextSearchQuery + "%"))
                    .build().list();
        } else if (afterTimestamp >= 0L) {
            results = daoSession.getMessageDao().queryBuilder()
                    .orderAsc(MessageDao.Properties.PostedTs)
                    .where(MessageDao.Properties.PostedTs.gt(afterTimestamp))
                    .where(MessageDao.Properties.Content.like("%" + fullTextSearchQuery + "%"))
                    .build().list();
        } else {
            results = daoSession.getMessageDao().queryBuilder()
                    .orderAsc(MessageDao.Properties.PostedTs)
                    .where(MessageDao.Properties.Content.like("%" + fullTextSearchQuery + "%"))
                    .build().list();
        }
        return results;
    }
}
