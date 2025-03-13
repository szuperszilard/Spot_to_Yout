package spotify;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {


        RetrieveTokenJSON retrieveToken = new RetrieveTokenJSON();
        String tokenToUse = retrieveToken.getAccessToken().toString();

        HttpRequest getRequest = new RequestBuilder("7H5rfvKonEwJ8waVaFlnRU", "?fields=total", tokenToUse).buildGET();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();

        Map<Integer, String> orderTrackArtist = new HashMap<>();
        int totalNumberOfTracks = Integer.parseInt(getResponse.body().replace("{\"total\":", "").replace("}", ""));
        int currentLastProcessed = 0;
        int remaining = totalNumberOfTracks;
        int limit = (Math.min(remaining, 50));
        int songCounter = 1;

        if(totalNumberOfTracks > 50){
            while(remaining > 0){
                getRequest = new RequestBuilder("7H5rfvKonEwJ8waVaFlnRU",
                        "?fields=items(track(name))&offset=" + currentLastProcessed + "&limit=" + limit,
                        tokenToUse).buildGET();
                getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
                String response = getResponse.body();
                System.out.println(response);
                remaining -= limit;
                currentLastProcessed += limit;
                Items items = gson.fromJson(getResponse.body(), Items.class);
                System.out.println(items.toString());
            }

        }

    }
}
