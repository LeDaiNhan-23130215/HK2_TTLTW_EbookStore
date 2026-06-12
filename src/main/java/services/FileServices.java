package services;

import DAO.FileDAO;
import models.File;

public class FileServices {
    public int createFileAndReturnIdForPdfFile(String pdfFileName, long size, String url) {
        File file = new File(pdfFileName, "pdf", size, url, "ACTIVE");
        return FileDAO.insertAndReturnIdForPdf(file);
    }

    public File getFileByID(int id){
        return new FileDAO().getFileById(id);
    }
}
