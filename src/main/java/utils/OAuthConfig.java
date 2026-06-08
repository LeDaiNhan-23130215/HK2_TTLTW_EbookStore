package utils;

import java.io.InputStream;
import java.util.Properties;

public class OAuthConfig {
    private static final Properties props = new Properties();

    static {
        try {
            InputStream input =
                    OAuthConfig.class.getClassLoader().getResourceAsStream("oauth.properties");
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load oauth.properties", e);
        }
    }

    public static String getClientId()       { return props.getProperty("google.client.id"); }
    public static String getClientSecret()   { return props.getProperty("google.client.secret"); }
    public static String getRedirectUriLocal() { return props.getProperty("google.redirect.uri.local"); }
    public static String getRedirectUriProd()  { return props.getProperty("google.redirect.uri.prod"); }
}