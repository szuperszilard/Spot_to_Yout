package spotify_to_youtube;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException, GeneralSecurityException {

        Map<Integer, String> orderTrackArtist = new HashMap<>();
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();
        SpotifyRetrieveTokenJSON retrieveToken = new SpotifyRetrieveTokenJSON();              //
        String tokenToUse = retrieveToken.getAccessToken().toString();          // get access token
        HttpRequest getRequest = new SpotifyRequestBuilder(args[0], "/tracks?fields=total", tokenToUse).buildGET(); //
        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());


        int totalNumberOfTracks = Integer.parseInt(getResponse.body().replace("{\"total\":", "").replace("}", "")); // get total number of tracks, format response
        int remaining = totalNumberOfTracks;
        int limit = Math.min(remaining,50);
        int currentLastProcessed = 0;
        int songCounter = 1;                            //to have the songs in exact order of playlist

        getRequest = new SpotifyRequestBuilder(args[0], "?fields=name", tokenToUse).buildGET();
        getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        String nameOfPlaylist = getResponse.body().replace("{\"name\":", "").replace("}", "");

        while(remaining > 0){
            getRequest = new SpotifyRequestBuilder(args[0],
                    "/tracks?fields=items(track(name,artists(name)))&offset=" + currentLastProcessed + "&limit=" + limit,
                    tokenToUse).buildGET();                                                                                         // get first json line
            getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            remaining -= limit;
            currentLastProcessed += limit;
            String stringResponse = getResponse.body().replace("{\"items\":", "");                              // format response
            stringResponse = stringResponse.substring(0, stringResponse.length()-1);
            Item[] item = gson.fromJson(stringResponse, Item[].class);                                                          //object array from json string
            for (Item value : item) {                                                                       //
                StringBuilder toPutIntoMap = new StringBuilder();                                                       //
                for (int j = 0; j < value.getTrack().getArtists().length; j++) {                                        //
                    if (j == 1 || j == 2 || j == 3 || j == 4) {                                                           //
                        toPutIntoMap.append(", ");                                                                      //
                    }                                                                                                   //
                    toPutIntoMap.append(value.getTrack().getArtists()[j].getName());                                  //
                }                                                                                                       //
                toPutIntoMap.append(" - ").append(value.getTrack().getName());                                                //
                orderTrackArtist.put(songCounter, toPutIntoMap.toString());                                             //
                songCounter++;                                                                                          //
            }                                                                                                           // add all the answers to Map
        }
        FileWriter writer = new FileWriter("D:\\REPO\\node\\songs.txt");
        for(int i = 1; i <= orderTrackArtist.size(); i++){
            writer.append(orderTrackArtist.get(i));
            if(i < orderTrackArtist.size()){
                writer.append("\n");
            }
        }
        writer.close();

        String directoryPath = "D:\\REPO\\node";
        String toRunFileName = "node finalMaybe.js songs.txt " + nameOfPlaylist;
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", toRunFileName);
        processBuilder.directory(new File(directoryPath));
        processBuilder.start();
    }
}