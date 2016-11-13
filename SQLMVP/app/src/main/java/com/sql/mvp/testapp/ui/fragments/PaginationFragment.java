package com.sql.mvp.testapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

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

import butterknife.BindString;
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

//TODO provide PaginationPresenter

public class PaginationFragment extends Fragment {

    private final static int LIMIT = 50;
    private PagingRecyclerViewAdapter recyclerViewAdapter;
    private Subscription pagingSubscription;
    private LocalStorage storage;

    @Inject
    Database<UsersData> usersDatabase;

    @BindView(R.id.pagination_list)
    RecyclerView recyclerView;

    @BindView(R.id.pagination_cached_elements_count_edit_text)
    EditText cachedElementsEditText;

    @BindString(R.string.pagination_error)
    String error;

    @OnClick(R.id.users_load_db)
    public void loadElementsToDB() {
    }

    @OnClick(R.id.users_delete_db)
    public void deleteElementsFromDB() {
        usersDatabase.deleteElements();
        Timber.e("usersDatabase.deleteElements =  " + usersDatabase.getCount());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.screen_pagination, container, false);
//        setRetainInstance(true);

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

        if (storage.getCachedLimit() == 0) {
            storage.setCachedLimit(LIMIT);
        }

        cachedElementsEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                String cachedElements = cachedElementsEditText.getText().toString();
                int maxLimit = EmulateResponseManager.MAX_LIMIT;

                if (TextUtils.isEmpty(cachedElements) && Integer.valueOf(cachedElements) == 0 &&
                        Integer.valueOf(cachedElements) > maxLimit) {
                    storage.setCachedLimit(Integer.valueOf(cachedElements));
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                return true;
            }
            return false;
        });

        // RecyclerView pagination
        PaginationTool<List<UsersData>> paginationTool = PaginationTool.
                buildPagingObservable(recyclerView, offset -> EmulateResponseManager
                        .getInstance()
                        .getEmulateResponse(offset, storage.getCachedLimit()))
                .setLimit(storage.getCachedLimit())
                .build();

        pagingSubscription = paginationTool
                .getPagingObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UsersData>>() {
                    @Override
                    public void onCompleted() {
                        Timber.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("onError");
                    }

                    @Override
                    public void onNext(List<UsersData> items) {
                        Timber.e("onNext");

//                        listElements.addAll(items);
//                        usersDatabase.insertOrUpdateElements(listElements);
//                        recyclerViewAdapter.addNewItems(listElements);

                        recyclerViewAdapter.addNewItems(items);
                        recyclerViewAdapter.notifyItemInserted(recyclerViewAdapter
                                .getItemCount() - items.size());
                    }
                });
    }

    @Override
    public void onDestroyView() {
        if (pagingSubscription != null && !pagingSubscription.isUnsubscribed()) {
            pagingSubscription.unsubscribe();
        }
        // for memory leak prevention (RecycleView is not unsubscibed from adapter DataObserver)
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }


        super.onDestroyView();
    }


}