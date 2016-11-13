/**
 * Copyright 2015 Eugene Matsyuk (matzuk2@mail.ru)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
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

/**
 * @author e.matsyuk
 */
public class PagingRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MAIN_VIEW = 0;

    private List<UsersData> listElements = new ArrayList<>();
    // after reorientation test this member
    // or one extra request will be sent after each reorientation
    private boolean allItemsLoaded;

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
        if (viewType == MAIN_VIEW) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users, parent, false);
            return new MainViewHolder(v);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return MAIN_VIEW;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case MAIN_VIEW:
                onBindTextHolder(holder, position);
                break;
        }
    }

    private void onBindTextHolder(RecyclerView.ViewHolder holder, int position) {
        MainViewHolder mainHolder = (MainViewHolder) holder;
        mainHolder.firstName.setText(getItem(position).getFirstName());
        mainHolder.lastName.setText(getItem(position).getLastName());
    }

}