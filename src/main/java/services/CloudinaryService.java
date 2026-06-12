package services;

import DAO.ImageDAO;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CloudinaryConfig;

import java.io.IOException;
import java.util.Map;

public class CloudinaryService {
    private static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);
    private static final String LOG_PREFIX = "[CLOUDINARY_SERVICE]";
    private final Cloudinary cloudinary =
            CloudinaryConfig.getCloudinary();

    public String uploadImageFromUrl(String imageUrl) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    imageUrl,
                    ObjectUtils.emptyMap()
            );
            return result.get("secure_url").toString();
        } catch (Exception e) {
            logger.error("{} Upload file failed", LOG_PREFIX, e);
            throw new RuntimeException("Upload image failed", e);
        }
    }

    public String uploadImageFromFile(Part filePart) {
        try {
            byte[] fileBytes = filePart.getInputStream().readAllBytes();
            Map<?, ?> result = cloudinary.uploader().upload(
                    fileBytes,
                    ObjectUtils.emptyMap()
            );
            return result.get("secure_url").toString();
        } catch (Exception e) {
            logger.error("{} Upload file failed", LOG_PREFIX, e);
            throw new RuntimeException("Upload file failed", e);
        }
    }

    public String uploadImage(Part filePart, String url) {
        if (filePart != null && filePart.getSize() > 0) {
            return uploadImageFromFile(filePart);
        }
        if (url != null && !url.isBlank()) {
            return uploadImageFromUrl(url);
        }
        throw new IllegalArgumentException("No image source provided");
    }

    public String uploadPdfFromUrl(String pdfUrl) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    pdfUrl,
                    ObjectUtils.asMap("resource_type", "raw"));
            return result.get("secure_url").toString();

        } catch (IOException e) {
            logger.error("{} Upload pdf file failed", LOG_PREFIX, e);
            throw new RuntimeException("Upload pdf file fail",e);
        }
    }

    public String uploadPdfFromFile(Part filePart) {
        try {
            byte[] fileBytes = filePart.getInputStream().readAllBytes();
            Map<?, ?> result = cloudinary.uploader().upload(
                    fileBytes,
                    ObjectUtils.asMap("resource_type", "raw")
            );
            return result.get("secure_url").toString();
        } catch (Exception e) {
            logger.error("{} Upload pdf file failed", LOG_PREFIX, e);
            throw new RuntimeException("Upload pdf file failed", e);
        }
    }

    public String uploadPdfFile(Part filePart, String url) {
        if (filePart != null && filePart.getSize() > 0) {
            return uploadPdfFromFile(filePart);
        }
        if (url != null && !url.isBlank()) {
            return uploadPdfFromUrl(url);
        }
        throw new IllegalArgumentException("No image source provided");
    }
}