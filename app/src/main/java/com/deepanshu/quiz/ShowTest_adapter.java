package com.deepanshu.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import static com.deepanshu.quiz.Login.MyPREFERENCES;
import static com.deepanshu.quiz.Login.user_id;

/**
 * Created by deepanshu on 27/7/17.
 */

public class ShowTest_adapter extends RecyclerView.Adapter<ShowTest_adapter.ViewHolder> {
    private List<ShowTest_ListItem> dt_listItems;
    Context context;
    public int status;
    //public String stat[] = {"Start","Stop"};
    public ShowTest_adapter(List<ShowTest_ListItem> listItems,Context context) {
        this.dt_listItems = listItems;
        this.context = context;
    }

    @Override
    public ShowTest_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.show_test_list,parent,false);

        return new ShowTest_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ShowTest_adapter.ViewHolder holder, int position) {
        ShowTest_ListItem listItem = dt_listItems.get(position);

        final String id = listItem.getTestId();
        String pass;
        pass = "Id : "+listItem.getTestId()+"    Name : "+listItem.getTestName();
        holder.mTestName.setText(pass);
        pass = "Topic : "+listItem.getTestTopic();
        holder.mTopic.setText(pass);
        pass = "Time : "+listItem.getTestDuration()+" Hrs";
        holder.mDuration.setText(pass);
        pass = "QB Id : "+listItem.getTestQBId();
        holder.mqbid.setText(pass);
        status = Integer.parseInt(listItem.getTestStatus());

        String temp = "";

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
