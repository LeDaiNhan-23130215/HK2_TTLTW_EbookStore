package mappers;

import models.Author;

public class AuthorCSVMapper implements CSVMapper<Author>{
    @Override
    public Author map(String[] cols) {
        String authorName = cols[0].trim();
        String authorDetail = cols[1].trim();

        if (authorName.isEmpty()) return null;

        return new Author(authorName, authorDetail);
    }
}
