package com.sql.mvp.testapp.ui.adatpers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sql.mvp.testapp.R;
import com.sql.mvp.testapp.utils.data.UsersData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class PagingRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UsersData> listElements = new ArrayList<>();
    // after reorientation test this member
    // or one extra request will be sent after each reorientation
    private boolean allItemsLoaded;

    public void addNewItems(List<UsersData> items) {
        if (items.size() == 0) {
            allItemsLoaded = true;
            return;
        }
        listElements.addAll(items);
    }

    public boolean isAllItemsLoaded() {
        return allItemsLoaded;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    public UsersData getItem(int position) {
        return listElements.get(position);
    }

    @Override
    public int getItemCount() {
        return listElements.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users, parent, false);
        return new MainViewHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindTextHolder(holder, position);

    }

    private void onBindTextHolder(RecyclerView.ViewHolder holder, int position) {
        MainViewHolder mainHolder = (MainViewHolder) holder;
        mainHolder.firstName.setText(getItem(position).getFirstName());
        mainHolder.lastName.setText(getItem(position).getLastName());

        Timber.e("first NAME = " + getItem(position).getFirstName());
        Timber.e("last NAME = " + getItem(position).getLastName());
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.first_name)
        TextView firstName;
        @BindView(R.id.last_name)
        TextView lastName;

        public MainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}