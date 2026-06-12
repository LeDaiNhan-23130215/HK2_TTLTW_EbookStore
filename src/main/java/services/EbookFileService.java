package services;

import DAO.EbookFileDAO;
import models.EbookFile;

public class EbookFileService {

    private final EbookFileDAO
            ebookFileDAO =
            new EbookFileDAO();

    public void addFileToBook(
            int ebookID,
            int fileID,
            boolean isDefault
    ){

        EbookFile ebookFile =
                new EbookFile(
                        ebookID,
                        fileID,
                        isDefault
                );

        ebookFileDAO.insert(
                ebookFile
        );
    }
}