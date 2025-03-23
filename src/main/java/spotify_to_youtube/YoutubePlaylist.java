package spotify_to_youtube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.PlaylistStatus;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class YoutubePlaylist {

    private String id;

    public String getId() {
        return id;
    }

    public static String createPlaylist(String playlistTitle, String playlistDescription, String privacyStatus) throws GeneralSecurityException, IOException {

        YouTube ytService = YoutubeAuthorization.youtubeService();
        Playlist myPlaylist = new Playlist();
        PlaylistSnippet snippet = new PlaylistSnippet();
        snippet.setDescription(playlistDescription);
        snippet.setTitle(playlistTitle);
        myPlaylist.setSnippet(snippet);
        PlaylistStatus status = new PlaylistStatus();
        status.setPrivacyStatus(privacyStatus);
        myPlaylist.setStatus(status);
        YouTube.Playlists.Insert request = ytService.playlists().insert("snippet,status", myPlaylist);

        return String.valueOf(request.execute());
    }


}
