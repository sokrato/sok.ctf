package io.github.sokrato.security;

import com.google.common.hash.HashFunction;

@FunctionalInterface
public interface Hasher {
    String hash(byte[] input);

    static Hasher fromHashFunction(HashFunction hf) {
        return input -> hf.newHasher().putBytes(input).hash().toString();
    }
}
