package spotify;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RetrieveTokenJSON {

    final private String client_id = System.getenv("CLIENT_ID");
    final private String client_secret = System.getenv("CLIENT_SECRET");

    public RetrieveTokenJSON() throws URISyntaxException, IOException, InterruptedException {
    }

    Gson gson = new Gson();
    HttpRequest postRequest = new RequestBuilder().buildPOST(client_id, client_secret);

    HttpClient httpClient = HttpClient.newHttpClient();

    HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());


    public AccessToken getAccessToken (){
        return gson.fromJson(postResponse.body(), AccessToken.class);
    }




}
