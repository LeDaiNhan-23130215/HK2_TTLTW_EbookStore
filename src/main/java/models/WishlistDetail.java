package models;

import java.time.LocalDateTime;

public class WishlistDetail extends Base {
    private int wishlistID;
    private int bookID;
    private LocalDateTime addedAt;

    public WishlistDetail(int id) {
        super(id);
    }

    public WishlistDetail(int id, int wishlistID, int bookID, LocalDateTime addedAt) {
        super(id);
        this.wishlistID = wishlistID;
        this.bookID = bookID;
        this.addedAt = addedAt;
    }

    public int getWishlistID() {
        return wishlistID;
    }

    public void setWishlistID(int wishlistID) {
        this.wishlistID = wishlistID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
