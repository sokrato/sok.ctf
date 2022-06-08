package io.github.sokrato.util;

import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

import java.math.BigInteger;

public class HashCracker {

    private static final HashCracker _shared = new HashCracker();

    public static HashCracker shared() {
        return _shared;
    }

    public byte[] crack(ByteHasher hasher, String target) {
        byte[] expect = BaseEncoding.base16().decode(target);
        return crack(hasher, new byte[0], new byte[0], expect);
    }

    public byte[] crack(ByteHasher hasher, byte[] prefix, byte[] suffix, byte[] target) {
        ChoiceWalker cw = new ChoiceWalker(Text.ALPHA_NUM_PUNC);
        for (int len = 1; ; ++len) {
            byte[] buf = new byte[len + prefix.length + suffix.length];
            System.arraycopy(prefix, 0, buf, 0, prefix.length);
            byte[] res = cw.walk(buf, prefix.length, len, input -> {
                byte[] output = hasher.hash(input);
                return isMatch(target, output);
            });

            if (res != null) {
                return res;
            }
        }
    }

    private boolean isMatch(byte[] expect, byte[] actual) {
        for (int i = 0; i < expect.length; ++i) {
            if (expect[i] != actual[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String jwt = "sMiOpljiXycZ5LvkJSaKOPiDSYsalcvJdRqfpGiPLco";
        byte[] res = Hashing.hmacSha256("byte".getBytes())
                .newHasher()
                .putBytes("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoiMSIsInBhc3N3ZCI6IjIiLCJ1aWQiOiI4YjM3NDU2MC0yNmE3LTQ4NmItYjU5MC0zYTExODZhMTIwOWEiLCJyb2xlIjoiZ3Vlc3QifQ".getBytes())
                .hash()
                .asBytes();
        System.out.println(BaseEncoding.base64().encode(res));
//        HashCracker cracker = new HashCracker();
//        byte[] res = cracker.crack(ByteHasher.fromHashFunction(Hashing.md5()), "656ee6");
//        if (!"cZ?".equals(new String(res))) {
//            throw new RuntimeException("WTF");
//        }
    }
}
