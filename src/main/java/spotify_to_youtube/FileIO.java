package spotify_to_youtube;

import java.io.*;
import java.util.Scanner;

public class FileIO {

    public FileIO() {
    }

    public static void writeToFile(String whatToWrite, File file) throws IOException {

        FileWriter writer = new FileWriter(file);
        writer.append(whatToWrite).append("\n");

    }
    public static String[] readFromFileToArray(String pathToFile) throws IOException {

        File file = new File(pathToFile);
        Scanner sc = new Scanner(file);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int linesInFile = 0;
        while (reader.readLine() != null) linesInFile++;
        reader.close();
        String[] arrayOfSongURLs = new String[linesInFile];
        int arrayIndex = 0;

        while(sc.hasNextLine()){

            String songURL = sc.nextLine();
            arrayOfSongURLs[arrayIndex] = sc.nextLine().replace("https://music.youtube.com/", "");
            arrayIndex++;
        }
        sc.close();
        reader.close();
        return arrayOfSongURLs;
    }



}
