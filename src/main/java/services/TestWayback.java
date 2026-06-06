package services;

public class TestWayback {
    public static void main(String[] args) throws Exception {

        String imageUrl =
                "https://nhasachmienphi.com/images/thumbnail/nhasachmienphi-them-hoang.jpg";

        String api =
                "https://archive.org/wayback/available?url="
                        + java.net.URLEncoder.encode(imageUrl, "UTF-8");

        System.out.println(api);
    }
}
