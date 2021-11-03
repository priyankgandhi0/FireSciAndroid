package com.firesafetysci.FireSci.Main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;

public class CommonFunctions {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network;
            if (connectivityManager == null) {
                return false;
            } else {
                network = connectivityManager.getActiveNetwork();
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                if (networkCapabilities == null) {
                    return false;

                }
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
            }
        } else {
            if (connectivityManager == null) {
                return false;
            }
            if (connectivityManager.getActiveNetworkInfo() == null) {
                return false;
            }
            return connectivityManager.getActiveNetworkInfo().isConnected();
        }
    }
}
