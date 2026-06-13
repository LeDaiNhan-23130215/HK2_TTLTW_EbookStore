package services;

import DAO.EbookFileDAO;
import DAO.FileDAO;
import models.File;

public class FileServices {
    public int createFileAndReturnIdForPdfFile(String pdfFileName, String fileFormat, long size, String url) {
        File file = new File(pdfFileName, fileFormat, size, url, "ACTIVE");
        return FileDAO.insertAndReturnIdForPdf(file);
    }

    public File getFileByFormat(int ebookId, String fileFormat) {
        return EbookFileDAO.getFileByFormat(ebookId, fileFormat);
    }
}
