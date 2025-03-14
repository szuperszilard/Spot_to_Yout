package spotify;

import java.util.Arrays;

public class Item {

    public Track track;

    @Override
    public String toString() {
        return "Item{" +
                "track=" + track +
                '}';
    }
    public class Track {

        private String name;
        private Name[] artists;

        @Override
        public String toString() {
            return "Track{" +
                    "name='" + name + '\'' +
                    ", artists=" + Arrays.toString(artists) +
                    '}';
        }
    }
    public class Name{
        private String name;

        @Override
        public String toString() {
            return "Name{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
