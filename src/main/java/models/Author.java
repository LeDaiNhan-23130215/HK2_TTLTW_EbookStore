package models;

public class Author extends Base {
    private String authorName;
    private String authorDetail;

    public Author(int id, String authorName, String authorDetail) {
        super(id);
        this.authorName = authorName;
        this.authorDetail = authorDetail;
    }

    public Author(int id) {
        super(id);
    }

    public Author(String authorName, String authorDetail) {
        super(-1);
        this.authorName = authorName;
        this.authorDetail = authorDetail;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorDetail() {
        return authorDetail;
    }

    public void setAuthorDetail(String authorDetail) {
        this.authorDetail = authorDetail;
    }

}
