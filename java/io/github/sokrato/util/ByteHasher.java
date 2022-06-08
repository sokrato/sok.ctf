package io.github.sokrato.util;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public interface ByteHasher {
    byte[] hash(byte[] input);

    static ByteHasher fromHashFunction(HashFunction hf) {
        return input -> hf.newHasher().putBytes(input).hash().asBytes();
    }

    ByteHasher md5 = fromHashFunction(Hashing.md5());
    ByteHasher sha256 = fromHashFunction(Hashing.sha256());
}
