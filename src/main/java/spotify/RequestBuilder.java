package spotify;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

public class RequestBuilder {

    private String playlistID;
    private String token;
    private String modifiers;

    public RequestBuilder(String playlistID, String modifiers, String token) {
        this.playlistID = playlistID;
        this.token = token;
        this.modifiers = modifiers;
    }
    public RequestBuilder(){

    }

    public HttpRequest buildGET() throws URISyntaxException {

        return HttpRequest.newBuilder()
                .uri(new URI("https://api.spotify.com/v1/playlists/" + playlistID + "/tracks" + modifiers))
                .header("Authorization", "Bearer" + token)
                .GET()
                .build();
    }
    public HttpRequest buildPOST(String client_id, String client_secret) throws URISyntaxException {

        return HttpRequest.newBuilder()
                .uri(new URI("https://accounts.spotify.com/api/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials&client_id=" + client_id + "&client_secret=" + client_secret))
                .build();
    }

}
