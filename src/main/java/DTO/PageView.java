package DTO;

import java.util.List;

public class PageView<T> {

    private List<T> items;
    private int currentPage;
    private int totalPages;

    public PageView(List<T> items, int currentPage, int totalPages) {
        this.items = items;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public List<T> getItems() {
        return items;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
