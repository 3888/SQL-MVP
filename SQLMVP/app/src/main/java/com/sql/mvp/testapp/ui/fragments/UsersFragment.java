package com.sql.mvp.testapp.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemCreator;
import com.sql.mvp.testapp.R;
import com.sql.mvp.testapp.application.Application;
import com.sql.mvp.testapp.server.models.UsersObject;
import com.sql.mvp.testapp.ui.adatpers.UsersAdapter;
import com.sql.mvp.testapp.ui.presenters.UsersPresenter;
import com.sql.mvp.testapp.utils.LocalStorage;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class UsersFragment extends BaseFragment implements UsersPresenter.MvpView {

    @BindView(R.id.users_list)
    RecyclerView recyclerView;
    @BindString(R.string.users_loading)
    String loading;

    @Inject
    UsersPresenter presenter;

    public static final int MOCK_NETWORK_DELAY = 1000;

    private UsersAdapter adapter;
    private Paginate paginate;
    private LocalStorage storage;
    private Handler handler;
    private int offsetLimit = 2;
    private int page = 0;
    boolean needPagination = true;

    @OnClick(R.id.users_load_from_db)
    public void loadElementsFromDB() {
        Timber.e("******loadElementsFromDB******");
        presenter.getUsers(0, needPagination);
    }

    @OnClick(R.id.users_delete_db)
    public void deleteDB() {
        presenter.deleteUsers();
    }

    @OnClick(R.id.users_disconnect)
    public void disconnect() {
        Timber.e("******disconnect******");
//        storage.setStoredPageCount(0);
        page = 0;
        needPagination = false;

        if (paginate != null) {
            paginate.unbind();
        }
        adapter.clearList();

//        if (adapter != null) {
//            adapter.unbind();
//            adapter = null;
//    }

}

private class CustomLoadingListItemCreator implements LoadingListItemCreator {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.loading_row, parent, false);
        return new ViewHolderLoading(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderLoading viewHolderLoading = (ViewHolderLoading) holder;
        viewHolderLoading.loading_message.setText(String.format(loading, adapter.getItemCount()));
    }
}

static class ViewHolderLoading extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_loading_text)
    TextView loading_message;

    public ViewHolderLoading(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_users, container, false);

        Application
                .getComponent(getContext())
                .inject(this);

//        storage = SharP.getInstance(getActivity(), LocalStorage.class);
//        page = storage.getStoredPageCount();

        presenter.onAttachView(this, ButterKnife.bind(this, view));

        handler = new Handler();
        presenter.getUsers(0, needPagination);
        recyclerView.setAdapter(adapter);

//        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        paginate = Paginate.with(recyclerView, presenter)
                .setLoadingTriggerThreshold(offsetLimit)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(new CustomLoadingListItemCreator())
                .build();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDetachView();
    }

    @Override
    public void initAdapter(List<UsersObject> users) {
        Timber.e("initAdapter");
        if (adapter == null) {
            Timber.e("adapter == null");
            adapter = new UsersAdapter(getActivity(), users);

        } else {
            Timber.e("adapter != null");
            adapter.add(users);
        }
    }

    @Override
    public void addUsersToDB() {
        handler.postDelayed(mockDataRunnable, MOCK_NETWORK_DELAY);
    }

    /**
     * Runnable used to fake asynchronous network request
     */
    private Runnable mockDataRunnable = new Runnable() {
        @Override
        public void run() {
//            storage.setStoredPageCount(page++);
            page++;
            Timber.e("addUsersToDB page = " + page);
            Timber.e("needPagination = " + needPagination);
            presenter.getUsers(page, needPagination);
            presenter.loading(false);

        }
    };
}