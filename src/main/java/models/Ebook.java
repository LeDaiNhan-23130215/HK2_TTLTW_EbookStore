package models;

import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Ebook extends Base {
    private String title;
    private double price;
    private String description;
    private int fileID;
    private String status;
    private String eBookCode;
    private int categoryID;
    private List<Image> images;
    private List<Author> authors;

    public Ebook(int id,String eBookCode, String title, double price, String description, int categoryID, int fileID, String status) {
        super(id);
        this.title = title;
        this.price = price;
        this.description = description;
        this.status = status;
        this.categoryID = categoryID;
        this.eBookCode = eBookCode;
    }

    public Ebook( String eBookCode, String title, double price, String description, int categoryID, int fileID, String status) {
        super(-1);
        this.title = title;
        this.price = price;
        this.description = description;
        this.status = status;
        this.fileID = fileID;
        this.categoryID = categoryID;
        this.eBookCode = eBookCode;
    }

    public Ebook(int id) {
        super(id);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public  String getTitle() {
        return this.title;
    }

    public double getPrice() {
        return this.price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ebook)) return false;
        Ebook ebook = (Ebook) o;
        return this.id == ebook.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    public String getEBookCode() {
        return eBookCode;
    }

    public void setBookCode(String eBookCode) {
        this.eBookCode = eBookCode;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
