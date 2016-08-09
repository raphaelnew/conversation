package ru.irafa.conversation.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import java.util.List;

import ru.irafa.conversation.ConversationApp;
import ru.irafa.conversation.R;
import ru.irafa.conversation.adapter.ConversationSearchableAdapter;
import ru.irafa.conversation.databinding.FragmentConversationBinding;
import ru.irafa.conversation.model.Message;
import ru.irafa.conversation.presenter.ConversationPresenter;
import ru.irafa.conversation.search.MessagesSearchProvider;
import ru.irafa.conversation.search.SearchEngine;
import ru.irafa.conversation.search.SearchResult;

/**
 * Fragment showing list of messages from conversation.
 * Created by Raphael Gilyazitdinov on 06.08.16.
 */

public class ConversationFragment extends Fragment
        implements ConversationPresenter.OnConversationListener, SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener, SearchEngine.OnSearchListener<Message> {

    private FragmentConversationBinding mBinding;

    private ConversationPresenter mPresenter;

    private ConversationSearchableAdapter mAdapter;

    private SearchEngine<Message,MessagesSearchProvider> mSearchEngine;

    private MenuItem mSearchItem;

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
        mAdapter = new ConversationSearchableAdapter(getContext());
        mBinding.recyclerView.setHasFixedSize(true);
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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mSearchItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(mSearchItem, this);

        mSearchEngine = new SearchEngine<>(new MessagesSearchProvider(ConversationApp.getDaoSession(getContext())), this);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        // Configure the search view and add search related event listeners.
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        //// TODO: 07.08.16
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        //// TODO: 07.08.16
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (mSearchItem != null) {
            mSearchItem.collapseActionView();
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mSearchEngine.search(newText);
        return true;
    }

    @Override
    public void onSearchCompleted(boolean success,
            @Nullable SearchResult<Message> searchResult) {
        mAdapter.applySearchResult(searchResult);
        if (success && searchResult != null && !searchResult.getResults().isEmpty()) {
            //Scroll RecyclerView to first item in search results.
            int scrollToPosition = mAdapter.getDataItemPosition(searchResult.getResults().get(searchResult.getResults().size()-1));
            mBinding.recyclerView.getLayoutManager().scrollToPosition(scrollToPosition);
        }
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
            Snackbar.make(mBinding.fragmentConversation, message, Snackbar.LENGTH_LONG).setAction(
                    getString(R.string.button_retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mPresenter.refresh();
                        }
                    }).setActionTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                    .show();
        }
    }

    @Override
    public void onConversationChanged(@NonNull List<Message> messages) {
        mBinding.retryButton.setVisibility(View.GONE);
        mBinding.emptyTextview.setVisibility(View.GONE);
        if (mAdapter != null) {
            mAdapter.updateDataSet(messages);
        }
    }
}
