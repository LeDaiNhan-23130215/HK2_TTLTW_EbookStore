package models;

import java.io.Serializable;
import java.util.List;

public class EbookAuthors implements Serializable {
    private int ebookID;
    private List<Author> authors;
}
