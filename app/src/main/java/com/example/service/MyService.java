package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

import SepratePackage.aidlInterface;

public class MyService extends Service {
    public MyService() {
    }


    aidlInterface.Stub stub = new aidlInterface.Stub() {


        @Override
        public int PerformPreviousPlay() throws RemoteException {
            return  Log.d("PreviousSong","Previous Song Played");
        }

        @Override
        public int PerformCurrentPlay() throws RemoteException {
            return 0;
        }

        @Override
        public int PerformNextPlay() throws RemoteException {
            return 0;
        }

        @Override
        public int notifysonginfo(String name) throws RemoteException {
            return Log.d("NotifySong","Current Song Notified");
        }


    };
    @Override
    public IBinder onBind(Intent intent) {
       return stub;
    }

//    Parcelable.ClassLoaderCreator classLoaderCreator = new Parcelable.ClassLoaderCreator() {
//        @Override
//        public Object createFromParcel(Parcel source, ClassLoader loader) {
//            return TrackInfo.PARCELABLE_WRITE_RETURN_VALUE;
//        }
//
//        @Override
//        public Object createFromParcel(Parcel source) {
//            return null;
//        }
//
//        @Override
//        public Object[] newArray(int size) {
//            return new Object[0];
//
//        }
//    };
 TrackInfo trackInfo()
{

    return null;
}
}