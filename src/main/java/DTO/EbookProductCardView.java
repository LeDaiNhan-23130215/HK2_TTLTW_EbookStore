package DTO;

public class EbookProductCardView {
    private int id;
    private String title;
    private double price;
    private String imageLink;

    public EbookProductCardView(int id, String title, double price, String imageLink) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.imageLink = imageLink;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageLink() {
        return imageLink;
    }

    public double getPrice() {
        return price;
    }
}
