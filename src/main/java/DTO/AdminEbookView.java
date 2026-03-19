package DTO;

public class AdminEbookView {
    private int id;
    private String title;
    private String authorName;
    private String categoryName;
    private double price;

    public AdminEbookView(int id, String title,
                          String authorName,
                          String categoryName,
                          double price) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.categoryName = categoryName;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getPrice() {
        return price;
    }
}