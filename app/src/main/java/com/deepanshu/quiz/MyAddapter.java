package com.deepanshu.quiz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by deepanshu on 26/7/17.
 */

public class MyAddapter extends RecyclerView.Adapter<MyAddapter.ViewHolder>{

    private List<ListItem> listItems;
    private Context context;

    public MyAddapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);

        holder.textViewQbid.setText(listItem.getQbid());
        holder.textViewQbname.setText(listItem.getQbname());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewQbid;
        public TextView textViewQbname;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewQbid =(TextView) itemView.findViewById(R.id.textViewQbid);
            textViewQbname =(TextView) itemView.findViewById(R.id.textViewQbname);
        }
    }
}