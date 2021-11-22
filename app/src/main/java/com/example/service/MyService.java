package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import SepratePackage.aidlInterface;

public class MyService extends Service {
    public MyService() {
    }



    aidlInterface.Stub stub = new aidlInterface.Stub() {
    @Override
    public int PerformPreviousPlay() throws RemoteException {

        return Log.d("PreviousSong","Previous Song Played");


    }

    @Override
    public int PerformCurrentPlay() throws RemoteException {

        return   Log.d("CurrentSong","Current Song Played");

    }

    @Override
    public int PerformNextPlay() throws RemoteException {

        return Log.d("NextSong","Next Song Played");
    }


    };
    @Override
    public IBinder onBind(Intent intent) {
       return stub;
    }

}