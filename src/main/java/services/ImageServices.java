package services;

import DAO.EbookImageDAO;
import DAO.ImageDAO;
import models.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class ImageServices {

    private final ImageDAO imageDAO = new ImageDAO();
    private final EbookImageDAO ebookImageDAO = new EbookImageDAO();
    private final CloudinaryService cloudinaryService = new CloudinaryService();

    private static final Logger logger = LoggerFactory.getLogger(ImageDAO.class);
    private static final String LOG_PREFIX = "[IMAGE_SERVICE]";


    public List<Image> getImagesByEbookID(int ebookID) {
        List<Image> list = imageDAO.getByEbookID(ebookID);
        return list != null ? list : Collections.emptyList();
    }

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

    public void removeAllImagesOfEbook(int ebookID) {
        ebookImageDAO.removeByEbookID(ebookID);
    }


    public void migrateUnmigratedImages() {
        List<Integer> ids = imageDAO.getImageIdsForMigration();
        logger.info("{} Start migration: {} images", LOG_PREFIX, ids.size());
        for (Integer imageId : ids) {
            migrateSingleImage(imageId);
        }
        logger.info("Migration finished");
    }

    private void migrateSingleImage(Integer imageId) {
        try {
            String originalUrl = imageDAO.getOriginalUrl(imageId);

            if (originalUrl == null || originalUrl.isBlank()) {
                logger.warn("{} Image {} has empty URL", LOG_PREFIX, imageId);
                imageDAO.markFailed(imageId);
                return;
            }
            String cloudinaryUrl = cloudinaryService.uploadImageFromUrl(originalUrl);
            imageDAO.markMigrated(imageId, cloudinaryUrl);
            logger.info("{} Migrated image {} successfully", LOG_PREFIX, imageId);
        } catch (Exception e) {
            logger.error("{} Failed to migrate image id={}", LOG_PREFIX, imageId, e);
            imageDAO.markFailed(imageId);
        }
    }
}

