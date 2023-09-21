package nl.nielsvanbruggen.videostreamingplatform.global.util;

import org.apache.commons.lang3.RandomStringUtils;

public class TokenGeneratorUtil {
    private TokenGeneratorUtil() {}

    public static String generate(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
