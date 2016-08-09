package ru.irafa.conversation.util;

import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import ru.irafa.conversation.R;

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
        // Convert Unix timestamp to millisecond.
        timestamp *= 1000L;
        calendar.setTimeInMillis(timestamp);
        try {
            // We can use SimpleDateFormat.getDateTimeInstance() but in this test project we want to
            // see full date in comfortable format.
            SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm a",
                    Locale.getDefault());
            format.setTimeZone(TimeZone.getDefault());
            return format.format(calendar.getTime());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Highlights part of the text and set it to TextView.
     *
     * @param textView      {@link TextView} object to set text to.
     * @param highLightPart part of text to be highlighted.
     */
    public static void highLightText(@Nullable TextView textView, @Nullable String highLightPart) {
        if (textView == null || highLightPart == null || highLightPart.isEmpty()) {
            return;
        }
        int color = ContextCompat.getColor(textView.getContext(), R.color.colorAccent);
        SpannableStringBuilder sb = new SpannableStringBuilder(textView.getText());
        // String indexOf() is case sensitive, we work with lowercase version.
        String originalText = textView.getText().toString().toLowerCase();
        int startIndex = 0;
        int endIndex = 0;
        // highLightPart might occur multiple times in the same text.
        while (startIndex >= 0) {
            // Find start and end character positions of highlight part.
            startIndex = originalText.indexOf(highLightPart, endIndex);
            if (startIndex < 0) {
                endIndex = -1;
            } else {
                endIndex = startIndex + highLightPart.length();
            }
            if (startIndex < 0 || endIndex < 0 || startIndex == endIndex) {
                continue;
            }
            sb.setSpan(new ForegroundColorSpan(color), startIndex, endIndex,
                    0);
            sb.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex,
                    0);
        }
        if (sb.getSpans(0, originalText.length(), ForegroundColorSpan.class) != null) {
            // To avoid android warning "getTextAfterCursor on inactive InputConnection" more
            // efficient way.
            textView.setText(null);
            textView.append(sb);
        }
    }
}
