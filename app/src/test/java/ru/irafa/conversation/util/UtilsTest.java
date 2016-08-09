package ru.irafa.conversation.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.TimeZone;

import static junit.framework.Assert.assertEquals;

/**
 * Local unit test for {@link Utils} class methods.
 * Created by Raphael Gilyazitdinov on 09.08.16.
 */
public class UtilsTest {

    @Before
    public void setUp() throws Exception {
        //We set locale and timezone before tests.
        Locale locale = new Locale("en", "EN");
        Locale.setDefault(locale);

        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        TimeZone.setDefault(timeZone);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getReadableMessageDate() throws Exception {
        //Random dates
        Long dateTimestamp = 1470353783L;
        String expectedResult = "Thu, Aug 4, 2016 at 11:36 PM";

        String resultString = Utils.getReadableMessageDate(dateTimestamp);
        assertEquals(expectedResult, resultString);

        dateTimestamp = 1469186369L;
        expectedResult = "Fri, Jul 22, 2016 at 11:19 AM";

        resultString = Utils.getReadableMessageDate(dateTimestamp);
        assertEquals(expectedResult, resultString);
        //Edge cases
        dateTimestamp = -222L;
        expectedResult = "Wed, Dec 31, 1969 at 11:56 PM";

        resultString = Utils.getReadableMessageDate(dateTimestamp);
        assertEquals(expectedResult, resultString);

        dateTimestamp = 0L;
        expectedResult = "Thu, Jan 1, 1970 at 12:00 AM";

        resultString = Utils.getReadableMessageDate(dateTimestamp);
        assertEquals(expectedResult, resultString);
    }

}