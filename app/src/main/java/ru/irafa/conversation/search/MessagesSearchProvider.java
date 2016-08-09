package ru.irafa.conversation.search;

import org.greenrobot.greendao.query.Query;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
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

    @SuppressWarnings("FieldCanBeLocal")
    private final String SEARCH_DATE_FORMAT = "yyyy-MM-dd";

    @SuppressWarnings("FieldCanBeLocal")
    private final String SEARCH_TAG_BEFORE = "before:";

    @SuppressWarnings("FieldCanBeLocal")
    private final String SEARCH_TAG_AFTER = "after:";

    private SimpleDateFormat dateFormat;

    private String fullTextSearchQuery = null;

    private Long beforeTimestamp = -1L;

    private Long afterTimestamp = -1L;

    private Query dbQuery;

    public MessagesSearchProvider(@Nullable DaoSession daoSession) {
        super();
        // Make SQLite query with 'LIKE' operator which is used to match text values against a pattern
        // using wildcards. GreenDao has great implementation for that operator.
        //
        // We initialize DB query here because reuse more efficient than creating new one, we only
        // change parameters when needed.
        if (daoSession != null) {
            this.dbQuery = daoSession.getMessageDao().queryBuilder()
                    .orderAsc(MessageDao.Properties.PostedTs)
                    .where(MessageDao.Properties.PostedTs.lt(beforeTimestamp))
                    .where(MessageDao.Properties.PostedTs.gt(afterTimestamp))
                    .where(MessageDao.Properties.Content.like("%" + fullTextSearchQuery + "%"))
                    .build();
        }
    }

    @Override
    public void initSearchProvider() {
        this.dateFormat = new SimpleDateFormat(SEARCH_DATE_FORMAT, Locale.ENGLISH);
        this.dateFormat.setLenient(false);
    }

    @Override
    public SearchResult<Message> provideResult(String query) {
        SearchResult<Message> searchResult = new SearchResult<>();
        if (query == null || query.isEmpty()) {
            return searchResult;
        }
        validateTagsAndQuery(query);
        List<Message> results = getSearchResultsFromDB();

        if (results != null && !results.isEmpty()) {
            searchResult.setFullTextSearchQuery(fullTextSearchQuery);
            searchResult.setResults(results);
        }
        return searchResult;
    }

    /**
     * Validates search query, finds supported tags, prepare for data fetching from DB.
     */
    private void validateTagsAndQuery(@NonNull String query) {
        query = query.trim();
        fullTextSearchQuery = null;
        beforeTimestamp = -1L;
        afterTimestamp = -1L;

        StringBuffer sb = new StringBuffer();
        sb.append(query);
        Log.d("Search", sb.toString());
        sb = searchBeforeTag(sb);
        Log.d("Search", sb.toString());
        sb = searchAfterTag(sb);
        Log.d("Search", sb.toString());
        // sb = searchXXXTag(sb); //Example of new tag support
        // We can easily add support for new search tag/patter here passing
        // StringBuffer that is already cleaned from tags that recognised.
        fullTextSearchQuery = sb.toString().trim();
    }

    @SuppressWarnings("EmptyCatchBlock")
    private StringBuffer searchBeforeTag(@NonNull StringBuffer sb) {
        // Regex pattern (?i)\\s?\\bbefore:\\d{4}-[01]\\d-[0-3]\\d\\b\\s? - case-insensitive,
        // checks key word,
        // format 4 numbers for year, month can start from 0 or 1,
        // date can start with with numbers 0-3.
        // This pattern doesn't validate values only format, values will be validated later.
        Pattern beforePattern = Pattern
                .compile("(?i)\\s?\\b" + SEARCH_TAG_BEFORE + "\\d{4}-[01]\\d-[0-3]\\d\\b\\s?");
        Matcher beforeMatcher = beforePattern.matcher(sb.toString());
        // There might be multiple Tags of the same type, we only user last one for demo,
        // but we clean all of them.
        sb = new StringBuffer();
        String beforeTag = null;
        while (beforeMatcher.find()) {
            beforeMatcher.appendReplacement(sb, " ");
            beforeTag = beforeMatcher.group().trim().replace(SEARCH_TAG_BEFORE, "");
        }
        beforeMatcher.appendTail(sb);
        // Second step we validate that date is correct, can be parsed by SimpleDateFormat, this
        // will eliminate situations with wrong days of the month like february 30 etc.
        if (beforeTag != null && !beforeTag.isEmpty()) {
            try {
                Date beforeDate = dateFormat.parse(beforeTag);
                beforeTimestamp = beforeDate.getTime() / 1000L;// To unix timestamp
            } catch (ParseException ex) {
            }
        }
        return sb;
    }

    @SuppressWarnings("EmptyCatchBlock")
    private StringBuffer searchAfterTag(@NonNull StringBuffer sb) {
        // Regex pattern (?i)\\s?\\bafter:\\d{4}-[01]\\d-[0-3]\\d\\b\\s? - case-insensitive,
        // checks key word,
        // format 4 numbers for year, month can start from 0 or 1,
        // date can start with with numbers 0-3.
        // This pattern doesn't validate values only format, values will be validated later.
        Pattern afterPattern = Pattern
                .compile("(?i)\\s?\\b" + SEARCH_TAG_AFTER + "\\d{4}-[01]\\d-[0-3]\\d\\b\\s?");
        Matcher afterMatcher = afterPattern.matcher(sb.toString());
        // There might be multiple Tags of the same type, we only user last one for demo,
        // but we clean all of them.
        sb = new StringBuffer();
        String afterTag = null;
        while (afterMatcher.find()) {
            afterMatcher.appendReplacement(sb, " ");
            afterTag = afterMatcher.group().trim().replace(SEARCH_TAG_AFTER, "");
        }
        afterMatcher.appendTail(sb);
        // Second step we validate that date is correct, can be parsed by SimpleDateFormat, this
        // will eliminate situations with wrong days of the month like february 30 etc.
        if (afterTag != null && !afterTag.isEmpty()) {
            try {
                //We add one day to date so "After" is next day after the date entered.
                Date afterDate = dateFormat.parse(afterTag);
                afterDate.setTime(afterDate.getTime() + TimeUnit.DAYS.toMillis(1));
                afterTimestamp = afterDate.getTime() / 1000L;// To unix timestamp
            } catch (ParseException ex) {
            }
        }
        return sb;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private List<Message> getSearchResultsFromDB() {
        if (dbQuery == null) {
            return null;
        }
        List<Message> results;
        // Tweak DB search query parameters based on tags and FTS.
        if (beforeTimestamp >= 0L && afterTimestamp >= 0L) {
            dbQuery.setParameter(0, beforeTimestamp);
            dbQuery.setParameter(1, afterTimestamp);
        } else if (beforeTimestamp >= 0L) {
            dbQuery.setParameter(0, beforeTimestamp);
            dbQuery.setParameter(1, Long.MIN_VALUE);
        } else if (afterTimestamp >= 0L) {
            dbQuery.setParameter(0, Long.MAX_VALUE);
            dbQuery.setParameter(1, afterTimestamp);
        } else {
            dbQuery.setParameter(0, Long.MAX_VALUE);
            dbQuery.setParameter(1, Long.MIN_VALUE);
        }
        dbQuery.setParameter(2, "%" + fullTextSearchQuery + "%");
        results = dbQuery.list();
        return results;
    }
}
