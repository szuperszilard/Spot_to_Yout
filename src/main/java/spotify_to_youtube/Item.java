package spotify_to_youtube;

import java.util.Arrays;

public class Item {

    public Track track;

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    @Override
    public String toString() {
        return "Item{" +
                "track=" + track +
                '}';
    }
    public class Track {
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Name[] getArtists() {
            return artists;
        }

        public void setArtists(Name[] artists) {
            this.artists = artists;
        }

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Name{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
