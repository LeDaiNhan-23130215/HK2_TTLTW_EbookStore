package utils;

import com.cloudinary.Cloudinary;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class CloudinaryConfig {
    private static Cloudinary cloudinary;
    private static final Properties properties = new Properties();
    static {
        try {
            InputStream input =
                    DBConfig.class.getClassLoader().getResourceAsStream("cloudinary.properties");
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load db.properties", e);
        }
    }
    public static Cloudinary getCloudinary() {
        if (cloudinary == null) {
            cloudinary = new Cloudinary(Map.of(
                    "cloud_name", properties.getProperty("cloudinary.cloud_name"),
                    "api_key", properties.getProperty("cloudinary.api_key"),
                    "api_secret", properties.getProperty("cloudinary.api_secret"),
                    "secure", true
            ));
        }
        return cloudinary;
    }
}

