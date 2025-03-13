package spotify;

public class Track {

    private String name;
    private Artists artists;

    @Override
    public String toString() {
        return "Track{" +
                "name='" + name + '\'' +
                ", artists=" + artists +
                '}';
    }
}
