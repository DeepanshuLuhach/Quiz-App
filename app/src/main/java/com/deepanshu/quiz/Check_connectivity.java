package com.deepanshu.quiz;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static java.lang.Boolean.FALSE;


public class Check_connectivity {

    private boolean isConnected;
    private Context cxt;

    public Check_connectivity(Context cxt) {
        this.cxt = cxt;
        this.isConnected = FALSE;
    }

    public boolean getInternetStatus() {

        ConnectivityManager check = (ConnectivityManager)
                this.cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = check.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

}
