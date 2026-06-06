package services;

public class ImageMigrationRunner {
    public static void main(String[] args) {

        ImageServices service = new ImageServices();

        try {
            service.migrateUnmigratedImages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
