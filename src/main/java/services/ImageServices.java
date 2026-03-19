package services;

import DAO.EbookImageDAO;
import DAO.ImageDAO;
import models.Image;

import java.util.Collections;
import java.util.List;

public class ImageServices {

    private final ImageDAO imageDAO = new ImageDAO();
    private final EbookImageDAO ebookImageDAO = new EbookImageDAO();

    /* ================= GET ================= */

    public List<Image> getImagesByEbookID(int ebookID) {
        List<Image> list = imageDAO.getByEbookID(ebookID);
        return list != null ? list : Collections.emptyList();
    }

    public Image getThumbnail(int ebookID) {
        return imageDAO.getFirstImageByEbookID(ebookID);
    }

    /* ================= INSERT ================= */

    public void insertImages(int ebookID, List<String> urls, String ebookTitle) {
        if (urls == null || urls.isEmpty()) return;

        for (String url : urls) {
            Image image = new Image(
                    ebookTitle,
                    url,
                    "ACTIVE"
            );

            int imageID = imageDAO.insertAndReturnId(image);

            if (imageID > 0) {
                ebookImageDAO.insert(ebookID, imageID);
            }
        }
    }

    /* ================= DELETE (SOFT) ================= */

    public void removeAllImagesOfEbook(int ebookID) {
        ebookImageDAO.removeByEbookID(ebookID);
    }
    public static void main(String[] args) {
        ImageServices imgS = new ImageServices();
        System.out.println(imgS.getThumbnail(283).getImgLink());
    }
}

