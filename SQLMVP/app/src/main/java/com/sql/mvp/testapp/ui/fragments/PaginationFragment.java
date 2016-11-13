package com.sql.mvp.testapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.sql.mvp.testapp.R;
import com.sql.mvp.testapp.application.Application;
import com.sql.mvp.testapp.database.Database;
import com.sql.mvp.testapp.ui.adatpers.PagingRecyclerViewAdapter;
import com.sql.mvp.testapp.utils.LocalStorage;
import com.sql.mvp.testapp.utils.data.EmulateResponseManager;
import com.sql.mvp.testapp.utils.data.UsersData;
import com.sql.mvp.testapp.utils.pagination.PaginationTool;


import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.ad.sharp.api.SharP;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class PaginationFragment extends Fragment {

    private final static int LIMIT = 50;
    private PagingRecyclerViewAdapter recyclerViewAdapter;
    private Subscription pagingSubscription;
    private LocalStorage storage;
    private int storedCount = 0;

    @Inject
    Database<UsersData> usersDatabase;

    @BindView(R.id.pagination_list)
    RecyclerView recyclerView;

    @OnClick(R.id.users_load_db)
    public void loadElementsToDB() {
    }

    @OnClick(R.id.users_delete_db)
    public void deleteElementsFromDB() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.screen_pagination, container, false);
        setRetainInstance(true);

        ButterKnife.bind(this, rootView);
        Application
                .getComponent(getContext())
                .inject(this);

        storage = SharP.getInstance(getActivity(), LocalStorage.class);

        init(rootView, savedInstanceState);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init(View view, Bundle savedInstanceState) {
        GridLayoutManager recyclerViewLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerViewLayoutManager.supportsPredictiveItemAnimations();
        // init adapter for the first time
        if (savedInstanceState == null) {
            recyclerViewAdapter = new PagingRecyclerViewAdapter();
            recyclerViewAdapter.setHasStableIds(true);
        }
        recyclerView.setSaveEnabled(true);

        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        // if all items was loaded we don't need Pagination
        if (recyclerViewAdapter.isAllItemsLoaded()) {
            return;
        }

        // RecyclerView pagination
        PaginationTool<List<UsersData>> paginationTool = PaginationTool.
                buildPagingObservable(recyclerView, offset -> EmulateResponseManager
                        .getInstance()
                        .getEmulateResponse(offset, LIMIT))
                .setLimit(LIMIT)
                .build();

        pagingSubscription = paginationTool
                .getPagingObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UsersData>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<UsersData> items) {

//                        usersDatabase.insertOrUpdateElements(items);
//                        Timber.e("DB count = " + usersDatabase.getCount());
//                        recyclerViewAdapter.addNewItems(usersDatabase.getElements());

                        recyclerViewAdapter.addNewItems(items);
                        recyclerViewAdapter.notifyItemInserted(recyclerViewAdapter.getItemCount() - items.size());
                    }
                });
    }

    @Override
    public void onDestroyView() {
        if (pagingSubscription != null && !pagingSubscription.isUnsubscribed()) {
            pagingSubscription.unsubscribe();
            usersDatabase.deleteElements();
        }
        // for memory leak prevention (RecycleView is not unsubscibed from adapter DataObserver)
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }
        super.onDestroyView();
    }

//    private void saveToDB(List<UsersData> items) {
//        storedCount += items.size();
//        storage.setStoredCount(storedCount);
//        Timber.e("getStoredCount() = " + storage.getStoredCount());
//        usersDatabase.insertOrUpdateElements(items);
//    }


}
