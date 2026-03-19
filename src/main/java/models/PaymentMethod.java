package models;

public class PaymentMethod extends Base {
    private String name;
    private String type;
    private String description;
    private int isActive;

    public PaymentMethod(int id) {
        super(id);
    }

    public PaymentMethod(int id, String name, String type, String description, int isActive) {
        super(id);
        this.name = name;
        this.type = type;
        this.description = description;
        this.isActive = isActive;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
