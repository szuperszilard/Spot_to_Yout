package spotify_to_youtube;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;


import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.List;

public class YoutubeAuthorization {

    private static final String CLIENT_SECRETS = "D:\\REPO\\Spot_to_Yout\\src\\main\\java\\spotify_to_youtube\\client_secret.json";
    private static final Collection<String> SCOPES = List.of("https://www.googleapis.com/auth/youtube.force-ssl");
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public static Credential authorization(NetHttpTransport httpTransport) throws IOException {

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new BufferedReader(new InputStreamReader(new FileInputStream(CLIENT_SECRETS))));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES).build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("szuperszilard");
    }

    public static YouTube youtubeService() throws GeneralSecurityException, IOException {

        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorization(httpTransport);

        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName("Spot_to_Yout").build();
    }

}
