package ru.irafa.conversation.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.irafa.conversation.ConversationApp;
import ru.irafa.conversation.R;
import ru.irafa.conversation.adapter.ConversationAdapter;
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

    private ConversationAdapter mAdapter;

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
        setupUI();
        if (savedInstanceState == null) {
            mPresenter = new ConversationPresenter(
                    ConversationApp.getDaoSession(getContext()), this);
        } else {
            mPresenter = ConversationPresenter.fromSavedState(
                    ConversationApp.getDaoSession(getContext()), this,
                    savedInstanceState);
        }
        return mBinding.getRoot();
    }



    private void setupUI() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        mAdapter = new ConversationAdapter(getContext());
        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.refresh();
            }
        });
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
        if (mAdapter != null && mAdapter.getItemCount() == 0) {
            mBinding.retryButton.setVisibility(View.GONE);
            mBinding.emptyTextview.setText(getString(R.string.status_loading));
            mBinding.emptyTextview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConversationEmpty() {
        if (mAdapter != null && mAdapter.getItemCount() == 0) {
            mBinding.retryButton.setVisibility(View.GONE);
            mBinding.emptyTextview.setText(getString(R.string.status_empty));
            mBinding.emptyTextview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConversationLoadingError(String message) {
        if (mAdapter != null && mAdapter.getItemCount() == 0) {
            mBinding.emptyTextview
                    .setText(String.format(getString(R.string.status_error), message));
            mBinding.emptyTextview.setVisibility(View.VISIBLE);
            mBinding.retryButton.setVisibility(View.VISIBLE);
        }else {
            // TODO: 07.08.16 add snackbar if error occurs while we showing content from DB.
        }
    }

    @Override
    public void onConversationChanged(List<Message> messages) {
        mBinding.retryButton.setVisibility(View.GONE);
        mBinding.emptyTextview.setVisibility(View.GONE);
        if (mAdapter != null) {
            mAdapter.updateItems(messages);
        }
    }
}
