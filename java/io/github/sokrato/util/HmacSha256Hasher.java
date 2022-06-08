package io.github.sokrato.util;

import com.google.common.hash.Hashing;

/**
 * HmacSha256Hasher with payload, used to
 * crack the key.
 */
public class HmacSha256Hasher implements ByteHasher {
    private final byte[] payload;

    public HmacSha256Hasher(byte[] payload) {
        this.payload = payload;
    }

    @Override
    public byte[] hash(byte[] key) {
        return Hashing.hmacSha256(key)
                .newHasher()
                .putBytes(payload)
                .hash()
                .asBytes();
    }
}
