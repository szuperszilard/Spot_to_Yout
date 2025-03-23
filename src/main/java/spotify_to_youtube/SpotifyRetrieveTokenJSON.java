package spotify_to_youtube;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SpotifyRetrieveTokenJSON {

    final private String client_id = System.getenv("CLIENT_ID");
    final private String client_secret = System.getenv("CLIENT_SECRET");

    public SpotifyRetrieveTokenJSON() throws URISyntaxException, IOException, InterruptedException {
    }

    Gson gson = new Gson();
    HttpRequest postRequest = new SpotifyRequestBuilder().buildPOST(client_id, client_secret);

    HttpClient httpClient = HttpClient.newHttpClient();

    HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());


    public SpotifyAccessToken getAccessToken (){
        return gson.fromJson(postResponse.body(), SpotifyAccessToken.class);
    }




}
