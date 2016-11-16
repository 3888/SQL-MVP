package com.sql.mvp.testapp.ui.adatpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.sql.mvp.testapp.R;
import com.sql.mvp.testapp.server.models.UsersObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolderItem> {

    private List<UsersObject> usersData;
    private final LayoutInflater inflater;
    private final Context context;


    public UsersAdapter(Context context, List<UsersObject> data) {
        this.context = context;
        this.usersData = data;
        this.inflater = LayoutInflater.from(context);

    }

    public void add(List<UsersObject> items) {
        int previousDataSize = usersData.size();
        this.usersData.addAll(items);
        notifyItemRangeInserted(previousDataSize, items.size());
    }


    @Override
    public UsersAdapter.ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderItem(context, inflater.inflate(R.layout.item_users, parent, false));
    }

    @Override
    public int getItemCount() {
        return usersData != null ? usersData.size() : 0;
    }



    @Override
    public void onBindViewHolder(ViewHolderItem holder, int position) {
        holder.bind(usersData.get(position));
    }

    static class ViewHolderItem extends RecyclerView.ViewHolder {
        @BindView(R.id.item_first_name)
        TextView first_name;
        @BindView(R.id.item_last_name)
        TextView last_name;

        private final Context context;

        ViewHolderItem(Context context, View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.context = context;
        }

        void bind(UsersObject users) {
            first_name.setText(users.getFirstName());
            last_name.setText(users.getLastName());

        }
    }

    public void unbind() {
        if (usersData != null) {
            usersData.clear();
            usersData = null;
        }
    }
}
