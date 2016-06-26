package com.titomilton.trackuserevents;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.titomilton.trackuserevents.rest.EventRequestMeta;

public class ConnectionInfo {

    public static String getConnectionType(Context context) throws NetworkConnectionNotFoundException {

        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                return EventRequestMeta.CONNECTION_WIFI;
            }else{
                return EventRequestMeta.CONNECTION_MOBILE;
            }
        }

        throw new NetworkConnectionNotFoundException();

    }

    public static boolean isConnected(Context context){
        try {
            getConnectionType(context);
            return true;
        } catch (NetworkConnectionNotFoundException e) {
            return false;
        }

    }

}
