package ru.irafa.conversation.util;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ru.irafa.conversation.R;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * Utility methods.
 * Created by Raphael Gilyazitdinov on 07.08.16.
 */

public class Utils {

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

    /**
     * Highlights part of the test in TextView.
     *
     * @param textView      {@link TextView} object apply text highlight to.
     * @param highLightPart text to be highlighted.
     */
    public static void highLightText(TextView textView, String highLightPart) {
        if (textView == null || highLightPart == null || highLightPart.isEmpty()) {
            return;
        }
        String originalText = textView.getText().toString();
        SpannableStringBuilder sb = new SpannableStringBuilder(originalText);
        // String indexOf() is case sensitive, we lowercase the original text but only after we
        // create SpannableStringBuilder so we don't break original text style.
        originalText = originalText.toLowerCase();
        int color = ContextCompat.getColor(textView.getContext(), R.color.colorAccent);
        int oldStartIndex = -1;
        int startIndex = 0;
        int endIndex = 0;
        // highLightPart might occur multiple times in the same text.
        while (oldStartIndex != startIndex) {
            //Find start and end character positions
            startIndex = originalText.indexOf(highLightPart, endIndex);
            endIndex = startIndex + highLightPart.length();
            if (startIndex < 0 || endIndex < 0) {
                break;
            }
            sb.setSpan(new ForegroundColorSpan(color), startIndex, endIndex,
                    SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex,
                    SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(sb);
    }
}
