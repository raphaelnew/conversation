package ru.irafa.conversation.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.irafa.conversation.R;

/**
 * Main Activity of the Conversation App. Shows {@link ru.irafa.conversation.fragment.ConversationFragment}.
 * Created by Raphael Gilyazitdinov on 06.08.16.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);
    }
}