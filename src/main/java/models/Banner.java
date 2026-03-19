package models;

public class Banner extends Base {
    private String url;
    private String position;
    private String startDate;
    private String endDate;
    private int isActive;

    public Banner(int id, String url, String position, String startDate, String endDate, int isActive) {
        super(id);
        this.url = url;
        this.position = position;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }

    public Banner(int id) {
        super(id);
    };
    public Banner(String url, String position, String startDate, String endDate, int isActive) {
        super(-1);
        this.url = url;
        this.position = position;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
