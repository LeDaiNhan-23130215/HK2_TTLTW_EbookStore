package models;

public class News extends Base {
    private String title;
    private String content;
    private String imgURL;
    private String author;
    private String publishedAt;
    private String createdAt;
    private int status;

    public News(int id) {
        super(id);
    }

    public News(int id, String title, String content, String imgURL,
                String author, String publishedAt, String createdAt, int status) {
        super(id);
        this.title = title;
        this.content = content;
        this.imgURL = imgURL;
        this.author = author;
        this.publishedAt = publishedAt;
        this.createdAt = createdAt;
        this.status = status;
    }

    public News(String title, String content, String imgURL, String author, int status) {
        super(-1);
        this.title = title;
        this.content = content;
        this.imgURL = imgURL;
        this.author = author;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
