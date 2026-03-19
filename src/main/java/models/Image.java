package models;

public class Image extends Base {
    private String imgName;
    private String imgLink;
    private String imgStatus;

    public Image(int id, String imgName, String imgLink, String imgStatus) {
        super(id);
        this.imgName = imgName;
        this.imgLink = imgLink;
        this.imgStatus = imgStatus;
    }

    public Image(String imgName, String imgLink, String imgStatus) {
        super(-1);
        this.imgName = imgName;
        this.imgLink = imgLink;
        this.imgStatus = imgStatus;
    }

    public Image(int id) {
        super(id);
    }

    public String getImgName() {
        return imgName;
    }

    public String getImgLink() {
        return imgLink;
    }

    public String getImgStatus() {
        return imgStatus;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public void setImgStatus(String imgStatus) {
        this.imgStatus = imgStatus;
    }
}
