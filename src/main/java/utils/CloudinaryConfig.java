package utils;

import com.cloudinary.Cloudinary;

import java.util.Map;

public class CloudinaryConfig {

    private static Cloudinary cloudinary;

    public static Cloudinary getCloudinary() {
        if (cloudinary == null) {
            cloudinary = new Cloudinary(Map.of(
                    "cloud_name", "dgyufq33a",
                    "api_key", "741297328974612",
                    "api_secret", "6s_U0YOk9lYllJToICYHzyquzq0",
                    "secure", true
            ));
        }
        return cloudinary;
    }
}

