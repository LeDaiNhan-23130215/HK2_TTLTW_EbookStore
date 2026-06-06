package services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import utils.CloudinaryConfig;

import java.util.Map;

public class CloudinaryService {

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
            throw new RuntimeException("Upload image failed", e);
        }
    }
}