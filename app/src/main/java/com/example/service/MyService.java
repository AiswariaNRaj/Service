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
    static ArrayList<TrackInfo> musicFiles;

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
        public boolean playPauseSong() throws RemoteException {
            boolean playPauseStatus;
            System.out.println ( "call reached to service " );
            if (mediaPlayer.isPlaying ()) {
                //btn_play_pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                mediaPlayer.pause ();
                playPauseStatus = true;
                System.out.println ( "Media player paused " + playPauseStatus );
                //Toast.makeText(getApplicationContext(),"Media player paused",Toast.LENGTH_SHORT).show();
            } else {
                //btn_play_pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                mediaPlayer.start ();
                playPauseStatus = false;
                System.out.println ( "Media player playing" + playPauseStatus );
                //Toast.makeText(getApplicationContext(),"Media player playing",Toast.LENGTH_SHORT).show();
            }
            return playPauseStatus;

        }

        @Override
        public List<String> getAllAudio() throws RemoteException {
            System.out.println ( "Entered int  to getAllAudio" );
            musicFiles = getAllAudioFile ( getApplicationContext () );
            System.out.println ( musicFiles );

            ArrayList<String> songTitle = new ArrayList<> ( musicFiles.size () );
            for (int i = 0; i < musicFiles.size (); i++) {
                songTitle.add ( musicFiles.get ( i ).getTitle () );
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
            System.out.println ( " playSong() - call reached to service,  position: " + position );
            System.out.println ( " currently playing song " + musicFiles.get ( position ).getTitle () );

            if (mediaPlayer != null) {
                mediaPlayer.stop ();
                mediaPlayer.release ();
            }

            String path = musicFiles.get ( position ).getPath ();
            mediaPlayer = new MediaPlayer ();
            try {
                mediaPlayer.setDataSource ( path );
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace ();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace ();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace ();
            }
            try {
                mediaPlayer.prepare ();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace ();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace ();
            }
            mediaPlayer.start ();
            if (mediaPlayer.isPlaying ()) {
                System.out.println ( "playing" );
            }
        }

        @Override
        public List<String> getSongDetails(int position) throws RemoteException {
            ArrayList<String> songDetails = new ArrayList<> ( musicFiles.size () );
            songDetails.add ( musicFiles.get ( position ).getTitle () );
            songDetails.add ( musicFiles.get ( position ).getAlbum () );
            songDetails.add ( musicFiles.get ( position ).getArtist () );

            songDetails.add ( String.valueOf ( musicFiles.size () ) );
            songDetails.add ( String.valueOf ( mediaPlayer.getDuration () ) );

            String uri = musicFiles.get ( position ).getPath ();
            System.out.println ( "uri" + uri );
            MediaMetadataRetriever retriever = new MediaMetadataRetriever ();
            retriever.setDataSource ( uri );
            byte[] art = retriever.getEmbeddedPicture ();
            System.out.println ( "byte  : " + art );
            retriever.release ();
            if (art != null) {
                String str = new String ( art );

                songDetails.add ( str );
            }

            System.out.println ( "file size " + musicFiles.size () );
            return songDetails;
        }

        @Override
        public int getcposition() throws RemoteException {
            System.out.println ( "getc position call reached to service " );
            return mediaPlayer.getCurrentPosition ();
        }

        public ArrayList<TrackInfo> getAllAudioFile(Context context) throws RemoteException {
            System.out.println ( " getAllAudio() call reached to service " );
            ArrayList<TrackInfo> tempAudioList = new ArrayList<> ();
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
                    tempAudioList.add ( musicFiles );
                }
                cursor.close ();
            }

            return tempAudioList;

        }
    };
}
