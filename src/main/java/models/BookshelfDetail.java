package models;

import java.time.LocalDateTime;

public class BookshelfDetail {
    private int id;
    private int bookshelfId;
    private int ebookId;
    private LocalDateTime addedAt;

    public BookshelfDetail() {}

    public BookshelfDetail(int id, int bookshelfId, int ebookId, LocalDateTime addedAt) {
        this.id = id;
        this.bookshelfId = bookshelfId;
        this.ebookId = ebookId;
        this.addedAt = addedAt;
    }

    public int getId() {
        return id;
    }

    public int getBookshelfId() {
        return bookshelfId;
    }

    public int getEbookId() {
        return ebookId;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }
}
