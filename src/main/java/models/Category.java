package models;

public class Category extends Base {
    private String name;
    private String description;
    private String icon;
    private String categoryCode;

    public Category(int id, String name, String description, String icon) {
        super(id);
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.categoryCode = null;
    }

    public Category(int id, String name, String description, String icon, String categoryCode) {
        super(id);
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.categoryCode = categoryCode;
    }

    public Category(String name, String description, String icon) {
        super(-1);
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.categoryCode = null;
    }

    public Category(String name, String description, String icon, String categoryCode) {
        super(-1);
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.categoryCode = categoryCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
}
