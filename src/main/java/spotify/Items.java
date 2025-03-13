package spotify;

import java.util.Arrays;

public class Items {

    private Track[] items;

    public Items(Track[] items) {
        this.items = items;
    }

    public Track[] getItems() {
        return items;
    }

    public void setItems(Track[] items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Items{" +
                "items=" + Arrays.toString(items) +
                '}';
    }
}
