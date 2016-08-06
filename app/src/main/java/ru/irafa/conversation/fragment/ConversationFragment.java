package ru.irafa.conversation.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.irafa.conversation.ConversationApp;
import ru.irafa.conversation.R;
import ru.irafa.conversation.databinding.FragmentConversationBinding;
import ru.irafa.conversation.model.Message;
import ru.irafa.conversation.presenter.ConversationPresenter;

/**
 * Fragment showing list of messages from conversation.
 * Created by Raphael Gilyazitdinov on 06.08.16.
 */

public class ConversationFragment extends Fragment
        implements ConversationPresenter.OnConversationListener {

    private FragmentConversationBinding mBinding;

    private ConversationPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_conversation, container, false);
        if (savedInstanceState == null) {
            mPresenter = new ConversationPresenter(
                    ((ConversationApp) getActivity().getApplication()).getDaoSession(), this);
        } else {
            mPresenter = ConversationPresenter.fromSavedState(
                    ((ConversationApp) getActivity().getApplication()).getDaoSession(), this,
                    savedInstanceState);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.conversation_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyView() {
        mPresenter.destroy();
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState(outState);
    }

    @Override
    public void onConversationLoading() {
        //// TODO: 06.08.16 show either loading empty view text or some sort of refresh indicator. 
    }

    @Override
    public void onConversationLoadingError(String message, boolean canRetry) {
        //// TODO: 06.08.16 show error message or snackback.
    }

    @Override
    public void onConversationChanged(List<Message> messages) {
        //// TODO: 06.08.16 update data in adapter.
    }
}
