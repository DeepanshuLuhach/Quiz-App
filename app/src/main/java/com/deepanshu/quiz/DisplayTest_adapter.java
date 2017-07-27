package com.deepanshu.quiz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by deepanshu on 27/7/17.
 */

public class DisplayTest_adapter extends RecyclerView.Adapter<DisplayTest_adapter.ViewHolder> {
    private List<DisplayTest_ListItem> dt_listItems;
    Context context;
    public DisplayTest_adapter(List<DisplayTest_ListItem> listItems,Context context) {
        this.dt_listItems = listItems;
        this.context = context;
    }

    @Override
    public DisplayTest_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_display_test__list_item,parent,false);

        return new DisplayTest_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DisplayTest_adapter.ViewHolder holder, int position) {
        DisplayTest_ListItem listItem = dt_listItems.get(position);

        String pass;
        pass = "Id : "+listItem.getTestId()+"    Name : "+listItem.getTestName();
        holder.mTestName.setText(pass);
        pass = "Topic : "+listItem.getTestTopic();
        holder.mTopic.setText(pass);
        pass = "Time : "+listItem.getTestDuration()+" Hrs";
        holder.mDuration.setText(pass);
        pass = "QB Id : "+listItem.getTestQBId();
        holder.mqbid.setText(pass);
        int status = Integer.parseInt(listItem.getTestStatus());
        if(status == 1){
            holder.mStatus.setText("Stop");
            holder.mStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"Stop clicked",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            holder.mStatus.setText("Start");
            holder.mStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"Start clicked",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dt_listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTestName;
        public TextView mTopic;
        public TextView mDuration;
        public TextView mqbid;
        public Button mStatus;


        public ViewHolder(View itemView) {
            super(itemView);

            mTestName = itemView.findViewById(R.id.tv_test_name);
            mDuration = itemView.findViewById(R.id.tv_duration);
            mqbid =  itemView.findViewById(R.id.tv_qb_id);
            mTopic = itemView.findViewById(R.id.tv_topic);
            mStatus = itemView.findViewById(R.id.btn_start_stop);
        }
    }
}
