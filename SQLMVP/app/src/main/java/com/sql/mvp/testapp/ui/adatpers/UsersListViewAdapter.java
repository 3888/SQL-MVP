package com.sql.mvp.testapp.ui.adatpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.sql.mvp.testapp.R;
import com.sql.mvp.testapp.server.models.UsersObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersListViewAdapter extends BaseAdapter {

    private final Context context;
    private List<UsersObject> data;
    private final LayoutInflater inflater;

    public UsersListViewAdapter(Context context, List<UsersObject> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public void setValues(List<UsersObject> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.item_users, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.first_name.setText(data.get(position).getFirstName());
        holder.last_name.setText(data.get(position).getLastName());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_first_name)
        TextView first_name;
        @BindView(R.id.item_last_name)
        TextView last_name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}