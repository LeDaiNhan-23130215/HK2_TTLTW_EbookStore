package services;

import DAO.AuthorDAO;
import DAO.EbookAuthorDAO;
import models.Author;

import java.util.Collections;
import java.util.List;

public class AuthorService {

    private final AuthorDAO authorDAO = new AuthorDAO();
    private final EbookAuthorDAO ebookAuthorDAO = new EbookAuthorDAO();

    /* ================= GET ================= */

    public Author getById(int id) {
        return authorDAO.getById(id);
    }

    public List<Author> getAll() {
        List<Author> list = authorDAO.findAll();
        return list != null ? list : Collections.emptyList();
    }

    public List<Author> getAuthorsByEbook(int ebookID) {
        return authorDAO.getByEbookID(ebookID);
    }

    /* ================= INSERT ================= */

    public int createAuthor(Author author) {
        return authorDAO.insertAndReturnId(author);
    }

    public void insert(int eBookID, int authorID) {
        ebookAuthorDAO.insert(eBookID, authorID);
    }

    public boolean addAuthor(Author author) {
        return authorDAO.addAuthor(author);
    }

    /* ================= UPDATE ================= */

    public boolean updateAuthor(Author author) {
        return authorDAO.update(author);
    }

    /* ================= DELETE ================= */

    public boolean deleteAuthor(int id) {
        return authorDAO.delete(id);
    }

    /* ================= LINKING ================= */

    public void assignAuthorsToEbook(int ebookID, List<Integer> authorIDs) {
        // Xóa liên kết cũ trước (khi update ebook)
        ebookAuthorDAO.removeByEbook(ebookID);

        // Gán lại
        for (Integer authorID : authorIDs) {
            ebookAuthorDAO.linkAuthorToEbook(ebookID, authorID);
        }
    }
}
