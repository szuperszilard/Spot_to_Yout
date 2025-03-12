package spotify;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {


        RetrieveTokenJSON retrieveToken = new RetrieveTokenJSON();
        String tokenToUse = retrieveToken.getAccessToken().toString();

        HttpRequest getRequest = new RequestBuilder("7H5rfvKonEwJ8waVaFlnRU", "?fields=total", tokenToUse).buildGET();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(getResponse.body());
    }
}
