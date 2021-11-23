package com.example.service;

import android.os.Parcel;
import android.os.Parcelable;

public class TrackInfo implements Parcelable {
    private String SongUrl;
    private String SongName;
    private String ArtistName;
    private String AlbumName;


    public TrackInfo(String songUrl, String songName, String artistName, String albumName) {
        SongUrl = songUrl;
        SongName = songName;
        ArtistName = artistName;
        AlbumName = albumName;
    }


    public String getSongUrl() {
        return SongUrl;
    }

    public void setSongUrl(String songUrl) {
        SongUrl = songUrl;
    }

    public String getSongName() {
        return SongName;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public String getArtistName() {
        return ArtistName;
    }

    public void setArtistName(String artistName) {
        ArtistName = artistName;
    }

    public String getAlbumName() {
        return AlbumName;
    }

    public void setAlbumName(String albumName) {
        AlbumName = albumName;
    }

    public static Creator<TrackInfo> getCREATOR() {
        return CREATOR;
    }

    protected TrackInfo(Parcel in) {

        String[] data = new String[4];
        this.SongUrl = data[0];
        this.SongName = data[1];
        this.ArtistName = data[2];
        this.AlbumName = data[3];
    }

    public static final Creator<TrackInfo> CREATOR = new Creator<TrackInfo>() {
        @Override
        public TrackInfo createFromParcel(Parcel in) {
            return new TrackInfo(in);
        }

        @Override
        public TrackInfo[] newArray(int size) {
            return new TrackInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.SongUrl,this.SongName,this.ArtistName,this.AlbumName
        });
    }
}
