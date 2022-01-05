// aidlInterface.aidl
package SepratePackage;

// Declare any non-default types here with import statements

interface aidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//   int PerformPreviousPlay();
//   int PerformCurrentPlay();
//   int PerformNextPlay();
//   int notifysonginfo(String name);
       boolean playPauseSong();
       void playSong(int position);
       List<String> getAllAudio();
       String getAlbum(int position);
       String getArtist(int position);
       List<String> getSongDetails(int position);
       int getcposition();
}