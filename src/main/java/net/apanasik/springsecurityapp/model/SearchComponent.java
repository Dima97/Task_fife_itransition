package net.apanasik.springsecurityapp.model;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class SearchComponent {

    private String image;
    private String description;
    private String price;

    public SearchComponent() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchComponent that = (SearchComponent) o;

        if (!image.equals(that.image)) return false;
        if (!description.equals(that.description)) return false;
        return price.equals(that.price);
    }

    @Override
    public int hashCode() {
        int result = image.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + price.hashCode();
        return result;
    }
}
