package com.sergeyza.shortenurl.util;

/**
 * Utility class for encoding numeric token values into compact Base62 short strings.
 * Load vs Length Rationale
 * If we expect to generate X requests per second and store short URLs for 10 years:
 * total = X * 60 * 60 * 24 * 365 * 10
 * Example: X = 100 req/sec = 100*60*60*24*365*10 → total ≈ 31.5 billion URLs
 * To hold that many values, we need to ensure that 62^N ≥ total
 * Which gives the length of the short URL:
 *      N = 6 → ~56B (enough)
 *      N = 7 → ~3.5T (future-proof)
 * This class encodes numbers into a fixed-length Base62 token using characters:
 *      [0-9a-zA-Z]
 */
public class TokenEncoder {

    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = BASE62_CHARS.length();

    public static String encode(long number, int minLength) {
        if (number <= 0) {
            throw new IllegalArgumentException("Token must be positive.");
        }
        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            sb.append(BASE62_CHARS.charAt((int) (number % BASE)));
            number /= BASE;
        }
        return String.format("%" + minLength + "s", sb.reverse().toString()).replace(' ', '0');
    }
}
