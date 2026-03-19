package DTO;

import java.util.List;
import java.util.stream.Collectors;

public class EbookFilterView {
    private Boolean free; // true = free, false = paid, null = all
    private List<Integer> categoryId;
    private List<String> formats;

    private String sortBy; // title, price, created_at
    private String sortDir; // asc, desc

    private int page;
    private int pageSize;

    private String keywords;
    // ===== GETTERS =====
    public Boolean getFree() {
        return free;
    }

    public List<Integer> getCategoryId() {
        return categoryId;
    }

    public List<String> getFormats() {
        return formats;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getSortDir() {
        return sortDir;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getKeywords() {return keywords;}
    // ===== SETTERS =====
    public void setFree(Boolean free) {
        this.free = free;
    }

    public void setCategoryId(List<Integer> categoryId) {
        this.categoryId = categoryId;
    }

    public void setFormats(List<String> formats) {
        this.formats = formats;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    // ===== HELPER METHODS =====
    public String getCategoryString() {
        if (categoryId == null || categoryId.isEmpty()) return "";
        return categoryId.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    public boolean hasCategory(Integer id) {
        return categoryId != null && categoryId.contains(id);
    }

    public boolean hasFormat(String format) {
        return formats != null && formats.contains(format);
    }

    public boolean isFreeChecked() {
        return Boolean.TRUE.equals(free);
    }

    public boolean isPaidChecked() {
        return Boolean.FALSE.equals(free);
    }

    public String getFormatString() {
        if (formats == null || formats.isEmpty()) return "";
        return String.join(",", formats);
    }

    public void setKeywords(String keyword) {
        this.keywords = keyword;
    }
}