package services;

import DAO.AuthorDAO;
import DAO.BookshelfDAO;
import DAO.BookshelfDetailDAO;
import DAO.ImageDAO;
import models.Bookshelf;
import models.Ebook;
import models.Image;

import java.util.List;
import java.util.Map;

public class BookshelfService {

    private final BookshelfDAO bookshelfDAO;
    private final BookshelfDetailDAO bookshelfDetailDAO;
    private final AuthorDAO authorDAO;
    private final ImageDAO imageDAO;

    public BookshelfService() {
        this.authorDAO = new AuthorDAO();
        this.imageDAO = new ImageDAO();
        this.bookshelfDAO = new BookshelfDAO();
        this.bookshelfDetailDAO = new BookshelfDetailDAO();
    }

    public Bookshelf getOrCreateBookshelf(int userId) {
        return bookshelfDAO.getOrCreateBookShelf(userId);
    }

    public void addBookToBookshelf(int userId, int ebookId) {
        Bookshelf bookshelf = getOrCreateBookshelf(userId);

        if (bookshelf == null) {
            throw new IllegalStateException("Không thể tạo bookshelf cho userId=" + userId);
        }

        boolean exists = bookshelfDetailDAO.exists(bookshelf.getId(), ebookId);

        if (!exists) {
            bookshelfDetailDAO.addBook(bookshelf.getId(), ebookId);
        }
    }

    public List<Ebook> getBooksOfUser(int userId) {
        return bookshelfDetailDAO.getBooksByUser(userId);
    }

    public boolean userOwnsBook(int userId, int ebookId) {
        Bookshelf bookshelf = bookshelfDAO.getByUserId(userId);
        if (bookshelf == null) return false;

        return bookshelfDetailDAO.exists(bookshelf.getId(), ebookId);
    }

    public List<Ebook> getBooksOfUserWithDetails(int userId) {

        List<Ebook> ebooks = bookshelfDetailDAO.getBooksByUser(userId);

        for (Ebook ebook : ebooks) {

            ebook.setImages(
                    imageDAO.getByEbookID(ebook.getId())
            );

            ebook.setAuthors(
                    authorDAO.getByEbookID(ebook.getId())
            );
        }

        return ebooks;
    }
}