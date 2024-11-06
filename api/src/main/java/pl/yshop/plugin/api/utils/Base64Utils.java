package pl.yshop.plugin.api.utils;

import java.util.Base64;

public class Base64Utils {
    public static String decode(String input) {
        byte[] decodedBytes = Base64.getDecoder().decode(input);
        return new String(decodedBytes);
    }
}
