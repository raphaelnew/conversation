package ru.irafa.conversation.search;

import org.greenrobot.greendao.database.Database;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.os.Build;

import ru.irafa.conversation.BuildConfig;
import ru.irafa.conversation.dao.DaoMaster;
import ru.irafa.conversation.dao.DaoSession;
import ru.irafa.conversation.model.Message;
import ru.irafa.conversation.model.User;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Local Robolectric unit tests for {@link MessagesSearchProvider} class methods.
 * Created by Raphael Gilyazitdinov on 09.08.16.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
@RunWith(RobolectricTestRunner.class)
public class MessagesSearchProviderTest {

    private DaoSession mDaoSession;

    @Before
    public void setUp() throws Exception {
        // Create DB and fill with test set of data.
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(RuntimeEnvironment.application,
                "test-conversation-db");
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();

        User user1 = new User();
        user1.setId(1);
        user1.setName("Test user 1");
        user1.setAvatarUrl(
                "https://d10oy3rrrp8hu2.cloudfront.net/0135067f22cd3ac38d15d5fa8293db57_s195.jpg");

        User user2 = new User();
        user1.setId(2);
        user2.setName("Test user 2");
        user2.setAvatarUrl(
                "https://d10oy3rrrp8hu2.cloudfront.net/be790245ea68e86fa6c8013e0ffa7059_s195.jpg");

        mDaoSession.getUserDao().insert(user1);
        mDaoSession.getUserDao().insert(user2);

        Message message1 = new Message();
        message1.setId(1);
        message1.setUserId(user1.getId());
        message1.setContent("Test message");
        message1.setPostedTs(1469182334L);//Fri, 22 Jul 2016 10:12:14 GMT

        Message message2 = new Message();
        message2.setId(2);
        message2.setUserId(user2.getId());
        message2.setContent("Test message second user");
        message2.setPostedTs(1469186324L);//Fri, 22 Jul 2016 11:18:44 GMT

        Message message3 = new Message();
        message3.setId(3);
        message3.setUserId(user1.getId());
        message3.setContent("Test message for search for test");
        message3.setPostedTs(1467645634L);//Mon, 04 Jul 2016 15:20:34 GMT

        Message message4 = new Message();
        message4.setId(4);
        message4.setUserId(user1.getId());
        message4.setContent("Test !@#$%^&*()_+{}[]:;'|<>/?");
        message4.setPostedTs(1467425639L);//Sat, 02 Jul 2016 02:13:59 GMT

        mDaoSession.getMessageDao().insert(message1);
        mDaoSession.getMessageDao().insert(message2);
        mDaoSession.getMessageDao().insert(message3);
        mDaoSession.getMessageDao().insert(message4);
    }

    @Test
    public void testSearchResultObject() throws Exception {
        MessagesSearchProvider messagesSearchProvider = new MessagesSearchProvider(mDaoSession);
        SearchResult<Message> searchResult;
        // Normal scenario case 1
        String searchQuery = "test";
        String expectedFullTextSearchQuery = "test";
        int expectedResultSize = 4;

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertNotNull(searchResult);
        assertEquals(expectedFullTextSearchQuery, searchResult.getFullTextSearchQuery());
        assertEquals(expectedResultSize, searchResult.getResults().size());
        // Normal scenario case 2
        searchQuery = "no such message";

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertNotNull(searchResult);
        assertNull(searchResult.getFullTextSearchQuery());
        assertEquals(0, searchResult.getResults().size());
        // Edge case
        searchResult = messagesSearchProvider.provideResult(null);

        assertNotNull(searchResult);
        assertNull(searchResult.getFullTextSearchQuery());
        assertEquals(0, searchResult.getResults().size());

        // Edge case
        searchQuery = "!@#$%^&*()_+{}[]:;'|<>/?";
        expectedFullTextSearchQuery = "!@#$%^&*()_+{}[]:;'|<>/?";
        expectedResultSize = 1;

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertNotNull(searchResult);
        assertEquals(expectedFullTextSearchQuery, searchResult.getFullTextSearchQuery());
        assertEquals(expectedResultSize, searchResult.getResults().size());
    }

    @Test
    public void testFullTextSearch() throws Exception {
        MessagesSearchProvider messagesSearchProvider = new MessagesSearchProvider(mDaoSession);
        SearchResult<Message> searchResult;
        // Normal scenario case 1
        String searchQuery = "test";
        String expectedFullTextSearchQuery = "test";
        int expectedResultSize = 4;
        int expectedMessageId;

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertEquals(expectedFullTextSearchQuery, searchResult.getFullTextSearchQuery());
        assertEquals(expectedResultSize, searchResult.getResults().size());
        // Normal scenario case 2
        searchQuery = "user";
        expectedFullTextSearchQuery = "user";
        expectedResultSize = 1;
        expectedMessageId = 2;

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertNotNull(searchResult);
        assertEquals(expectedFullTextSearchQuery, searchResult.getFullTextSearchQuery());
        assertEquals(expectedResultSize, searchResult.getResults().size());
        assertEquals(expectedMessageId, searchResult.getResults().get(0).getId());
        // Normal scenario case 3
        searchQuery = "Test message ";
        expectedFullTextSearchQuery = "Test message";
        expectedResultSize = 3;


        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertNotNull(searchResult);
        assertEquals(expectedFullTextSearchQuery, searchResult.getFullTextSearchQuery());
        assertEquals(expectedResultSize, searchResult.getResults().size());
        // We expect order of items based on Timestamp so we check id based on it.
        assertEquals(3, searchResult.getResults().get(0).getId());
        assertEquals(1, searchResult.getResults().get(1).getId());
        assertEquals(2, searchResult.getResults().get(2).getId());
    }

    @Test
    public void testBeforeTag() throws Exception{
        MessagesSearchProvider messagesSearchProvider = new MessagesSearchProvider(mDaoSession);
        SearchResult<Message> searchResult;
        // Normal scenario case 1
        String searchQuery = "before:2016-07-04";
        String expectedFullTextSearchQuery = "";
        int expectedResultSize = 1;
        int expectedMessageId = 4;

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertEquals(expectedFullTextSearchQuery, searchResult.getFullTextSearchQuery());
        assertEquals(expectedResultSize, searchResult.getResults().size());
        assertEquals(expectedMessageId, searchResult.getResults().get(0).getId());
        // Normal scenario case 2
        searchQuery = "before:2016-07-22 ";
        expectedFullTextSearchQuery = "";
        expectedResultSize = 2;

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertEquals(expectedFullTextSearchQuery, searchResult.getFullTextSearchQuery());
        assertEquals(expectedResultSize, searchResult.getResults().size());
        assertEquals(4, searchResult.getResults().get(0).getId());
        assertEquals(3, searchResult.getResults().get(1).getId());
        // Edge case
        searchQuery = "before:2016-05-10";

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertNull(searchResult.getFullTextSearchQuery());
        assertEquals(0, searchResult.getResults().size());
    }

    @Test
    public void testAfterTag() throws Exception{
        MessagesSearchProvider messagesSearchProvider = new MessagesSearchProvider(mDaoSession);
        SearchResult<Message> searchResult;
        // Normal scenario case 1
        String searchQuery = "after:2016-07-04";
        String expectedFullTextSearchQuery = "";
        int expectedResultSize = 2;

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertEquals(expectedFullTextSearchQuery, searchResult.getFullTextSearchQuery());
        assertEquals(expectedResultSize, searchResult.getResults().size());
        assertEquals(1, searchResult.getResults().get(0).getId());
        assertEquals(2, searchResult.getResults().get(1).getId());
        // Normal scenario case 2
        searchQuery = " after:2016-07-01";
        expectedFullTextSearchQuery = "";
        expectedResultSize = 4;

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertEquals(expectedFullTextSearchQuery, searchResult.getFullTextSearchQuery());
        assertEquals(expectedResultSize, searchResult.getResults().size());
        // Edge case
        searchQuery = "after:2016-08-10";

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertNull(searchResult.getFullTextSearchQuery());
        assertEquals(0, searchResult.getResults().size());
    }

    @Test
    public void testBeforeAfterTags() throws Exception{
        MessagesSearchProvider messagesSearchProvider = new MessagesSearchProvider(mDaoSession);
        SearchResult<Message> searchResult;
        // Normal scenario case 1
        String searchQuery = "before:2016-07-22 after:2016-07-02";
        String expectedFullTextSearchQuery = "";
        int expectedResultSize = 1;
        int expectedMessageId = 3;

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertEquals(expectedFullTextSearchQuery, searchResult.getFullTextSearchQuery());
        assertEquals(expectedResultSize, searchResult.getResults().size());
        assertEquals(expectedMessageId, searchResult.getResults().get(0).getId());
        // Normal scenario case 2
        searchQuery = " after:2016-07-02 before:2016-07-24 ";
        expectedFullTextSearchQuery = "";
        expectedResultSize = 3;

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertEquals(expectedFullTextSearchQuery, searchResult.getFullTextSearchQuery());
        assertEquals(expectedResultSize, searchResult.getResults().size());
        // Edge case
        searchQuery = "before:2016-07-04 after:2016-07-04";

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertNull(searchResult.getFullTextSearchQuery());
        assertEquals(0, searchResult.getResults().size());
    }

    @Test
    public void testBeforeAfterFTSTags() throws Exception{
        MessagesSearchProvider messagesSearchProvider = new MessagesSearchProvider(mDaoSession);
        SearchResult<Message> searchResult;
        // Normal scenario case 1
        String searchQuery = "before:2016-07-22 !@#$%^&*() after:2016-06-15";
        String expectedFullTextSearchQuery = "!@#$%^&*()";
        int expectedResultSize = 1;
        int expectedMessageId = 4;

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertEquals(expectedFullTextSearchQuery, searchResult.getFullTextSearchQuery());
        assertEquals(expectedResultSize, searchResult.getResults().size());
        assertEquals(expectedMessageId, searchResult.getResults().get(0).getId());
        // Normal scenario case 2
        searchQuery = "after:2016-07-02 before:2016-07-26 search for test ";
        expectedFullTextSearchQuery = "search for test";
        expectedResultSize = 1;
        expectedMessageId = 3;

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertEquals(expectedFullTextSearchQuery, searchResult.getFullTextSearchQuery());
        assertEquals(expectedResultSize, searchResult.getResults().size());
        assertEquals(expectedMessageId, searchResult.getResults().get(0).getId());
        // Edge case
        searchQuery = "conversation before:2016-07-22 after:2016-07-02 ";

        searchResult = messagesSearchProvider.provideResult(searchQuery);

        assertNull(searchResult.getFullTextSearchQuery());
        assertEquals(0, searchResult.getResults().size());
    }
}