package com.example.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import SepratePackage.aidlInterface;


public class MyService extends Service {
    public static final int REQUEST_CODE = 1;
    static MediaPlayer mediaPlayer;
    static ArrayList<TrackInfo> musiclist;

    public MyService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        createNotificationChannel ();

        Intent intent1 = new Intent ( this, MainActivity.class );
        PendingIntent pendingIntent = PendingIntent.getActivity ( this, 0, intent1, 0 );
        Notification notification = new NotificationCompat.Builder ( this, "ChannelId1" ).setContentTitle ( "Service application" )
                .setContentText ( "Application Running" )
                .setSmallIcon ( R.mipmap.ic_launcher )
                .setContentIntent ( pendingIntent ).build ();
        startForeground ( 1, notification );

        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ;
        {
            NotificationChannel notificationChannel = new NotificationChannel (
                    "ChannelId1", "Foreground notification", NotificationManager.IMPORTANCE_DEFAULT );
            NotificationManager manager = getSystemService ( NotificationManager.class );
            manager.createNotificationChannel ( notificationChannel );
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart ( intent, startId );
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;

    }

    aidlInterface.Stub iBinder = new aidlInterface.Stub () {

        @Override
        public int PerformPreviousPlay() throws RemoteException {
            return 0;
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
            return 0;
        }

        @Override
        public boolean playPauseSong() throws RemoteException {
            boolean playPauseStatus;
            System.out.println ( "call reached to service " );
            if (mediaPlayer.isPlaying ()) {
                mediaPlayer.pause ();
                playPauseStatus = true;
            } else {
                mediaPlayer.start ();
                playPauseStatus = false;
            }
            return playPauseStatus;

        }

        @Override
        public List<String> getAllAudio() throws RemoteException {
            musiclist = getAllAudioFile ( getApplicationContext () );
            System.out.println ( musiclist );

            ArrayList<String> songTitle = new ArrayList<> ( musiclist.size () );
            for (int i = 0; i < musiclist.size (); i++) {
                songTitle.add ( musiclist.get ( i ).getTitle () );
                System.out.println ( songTitle );
            }
            return songTitle;

        }

        @Override
        public String getAlbum(int position) throws RemoteException {
            return null;
        }

        @Override
        public String getArtist(int position) throws RemoteException {
            return null;
        }

        @Override
        public void playSong(int position) throws RemoteException {

            if (mediaPlayer != null) {
                mediaPlayer.stop ();
                mediaPlayer.release ();
            }

            String path = musiclist.get ( position ).getPath ();
            mediaPlayer = new MediaPlayer ();
            try {
                mediaPlayer.setDataSource ( path );
            } catch (IllegalArgumentException e) {
                e.printStackTrace ();
            } catch (IllegalStateException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
            try {
                mediaPlayer.prepare ();
            } catch (IllegalStateException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
            mediaPlayer.start ();
            if (mediaPlayer.isPlaying ()) {
                Log.d (  "playing","Song Playing" );
            }
        }

        @Override
        public List<String> getSongDetails(int position) throws RemoteException {
            ArrayList<String> songDetails = new ArrayList<> ( musiclist.size () );
            songDetails.add ( musiclist.get ( position ).getTitle () );
            songDetails.add ( musiclist.get ( position ).getAlbum () );
            songDetails.add ( musiclist.get ( position ).getArtist () );

            songDetails.add ( String.valueOf ( musiclist.size () ) );
            songDetails.add ( String.valueOf ( mediaPlayer.getDuration () ) );

            String uri = musiclist.get ( position ).getPath ();
            System.out.println ( "uri" + uri );
            MediaMetadataRetriever retriever = new MediaMetadataRetriever ();
            retriever.setDataSource ( uri );
            byte[] art = retriever.getEmbeddedPicture ();
            retriever.release ();
            if (art != null) {
                String str = new String ( art );

                songDetails.add ( str );
            }


            return songDetails;
        }

        @Override
        public int getcurrentposition() throws RemoteException {
            return mediaPlayer.getCurrentPosition ();
        }

        public ArrayList<TrackInfo> getAllAudioFile(Context context) throws RemoteException {
            ArrayList<TrackInfo> tempList = new ArrayList<> ();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] projections = {
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ARTIST,
            };
            Cursor cursor = context.getContentResolver ().query ( uri, projections, null, null, null );
            if (cursor != null) {
                while (cursor.moveToNext ()) {
                    String album = cursor.getString ( 0 );
                    String title = cursor.getString ( 1 );
                    String duration = cursor.getString ( 2 );
                    String path = cursor.getString ( 3 );
                    String artist = cursor.getString ( 4 );

                    TrackInfo musicFiles = new TrackInfo ( path, title, artist, album, duration );

                    Log.e ( "Path : " + path, "Album: " + album );
                    tempList.add ( musicFiles );
                }
                cursor.close ();
            }

            return tempList;

        }
    };
}