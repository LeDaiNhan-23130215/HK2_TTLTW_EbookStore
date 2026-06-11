package models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Discount implements Serializable {

    private static final DateTimeFormatter DISPLAY_FMT =
            DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    private static final DateTimeFormatter INPUT_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private int id;
    private String name;
    private String description;
    // "PERCENT" hoặc "FIXED"
    private String discountType;
    private BigDecimal discountValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    // "ACTIVE" | "INACTIVE" | "ENDED"
    private String        status;

    public Discount() {}

    public Discount(int id, String name, String description,
                    String discountType, BigDecimal discountValue,
                    LocalDateTime startDate, LocalDateTime endDate, String status) {
        this.id= id;
        this.name= name;
        this.description= description;
        this.discountType= discountType;
        this.discountValue= discountValue;
        this.startDate= startDate;
        this.endDate= endDate;
        this.status= status;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getName(){ return name; }
    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String desc){
        this.description = desc;
    }

    public String getDiscountType(){
        return discountType;
    }
    public void setDiscountType(String type){
        this.discountType = type;
    }

    public BigDecimal getDiscountValue(){
        return discountValue;
    }
    public void setDiscountValue(BigDecimal value){
        this.discountValue = value;
    }

    public LocalDateTime getStartDate(){
        return startDate;
    }
    public void setStartDate(LocalDateTime d){
        this.startDate = d;
    }

    public LocalDateTime getEndDate(){
        return endDate;
    }
    public void setEndDate(LocalDateTime d){
        this.endDate = d;
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public String getFormattedStartDate() {
        return startDate != null ? startDate.format(DISPLAY_FMT) : "—";
    }

    public String getFormattedEndDate() {
        return endDate != null ? endDate.format(DISPLAY_FMT) : "—";
    }

    public String getStartDateForInput() {
        return startDate != null ? startDate.format(INPUT_FMT) : "";
    }

    public String getEndDateForInput() {
        return endDate != null ? endDate.format(INPUT_FMT) : "";
    }

}
