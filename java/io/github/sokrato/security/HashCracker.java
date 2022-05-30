package io.github.sokrato.security;

import com.google.common.base.Strings;
import io.github.sokrato.util.BytesGen;

public class HashCracker {

    // 可见的 ASCII 字符
    public static final byte[] ASCII = ("0123456789" +
            "abcdefghijklmnopqrstuvwxyz" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "!@#$%^&*()-_=+\\|/?,<.>").getBytes();

    private final byte[] alphabet;
    private final Hasher hasher;

    public HashCracker(Hasher hasher) {
        this(hasher, ASCII);
    }

    public HashCracker(Hasher hasher, byte[] alphabet) {
        if (alphabet == null || alphabet.length == 0) {
            throw new IllegalArgumentException("invalid alphabet");
        }
        this.hasher = hasher;
        this.alphabet = alphabet;
    }

    public String crack(String prefix, String target, int maxLen) {
        for (int len = 1; len < maxLen; ++len) {
            var res = crackFixedLength(prefix, target, len);
            if (!"".equals(res)) {
                return res;
            }
        }
        return "";
    }

    public String crackFixedLength(String prefix, String target, int len) {
        var buf = (prefix + Strings.repeat("*", len)).getBytes();
        var gen = new BytesGen(alphabet, buf, prefix.length(), len);
        var result = "";

        for (var it = gen.next(); it.isPresent(); it = gen.next()) {
            buf = it.get();
            var sha = hasher.hash(buf);
            if (sha.startsWith(target)) {
                result = new String(buf);
                break;
            }
        }

        if (!"".equals(result)) {
            return result.substring(prefix.length());
        }
        return result;
    }
}
