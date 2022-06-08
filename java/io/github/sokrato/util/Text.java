package io.github.sokrato.util;

public final class Text {
    public static final String DIGITS = "0123456789";
    public static final String LOWER_ASCII = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPER_ASCII = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHABET = LOWER_ASCII + UPPER_ASCII;
    public static final String ALPHA_NUM = ALPHABET + DIGITS;
    public static final String PUNCTUATION = "!\"#$%&'()*+,-./:;<=>?@[\\]^_";
    public static final String ALPHA_NUM_PUNC = ALPHA_NUM + PUNCTUATION;
}
