package ru.irafa.conversation.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Utility methods for working with Time and Date.
 * Created by Raphael Gilyazitdinov on 07.08.16.
 */

public class TimeDateUtils {

    /**
     * Returns readable date representation of message timestamp.
     *
     * @param timestamp to convert to readable date.
     * @return Formatted representation of timestamp date or empty string.
     */
    public static String getReadableMessageDate(Long timestamp) {
        Calendar calendar = Calendar.getInstance();
        //convert Unix timestamp to millisecond.
        timestamp *= 1000L;
        calendar.setTimeInMillis(timestamp);
        //Create a DateFormatter object for displaying date in specified format.
        try {
            //We can use SimpleDateFormat.getDateTimeInstance() but in this test project we want to
            // see full date.
            SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm a");
            return format.format(calendar.getTime());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "";
        }
    }
}
