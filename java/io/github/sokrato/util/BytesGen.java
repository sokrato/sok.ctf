package io.github.sokrato.util;

import java.util.Optional;

public class BytesGen implements Generator<byte[]> {
    private final byte[] alphabet;
    private final byte[] buf;
    private final int[] position;

    private final int offset;

    public BytesGen(byte[] alphabet, int len) {
        this(alphabet, new byte[len], 0, len);
    }

    public BytesGen(byte[] alphabet, byte[] buf, int offset, int len) {
        if (alphabet == null || alphabet.length == 0) {
            throw new IllegalArgumentException("invalid alphabet");
        }
        if (buf == null || offset + len > buf.length || offset < 0 || len < 1) {
            throw new IllegalArgumentException("invalid buf");
        }

        this.alphabet = alphabet;
        this.buf = buf;
        this.offset = offset;
        position = new int[len];
        position[len - 1] = -1;

        initBuf();
    }

    private void initBuf() {
        for (int i = offset; i < buf.length - 1; ++i) {
            buf[i] = alphabet[0];
        }
    }

    @Override
    public Optional<byte[]> next() {
        int carry = 1;
        for (int i = position.length - 1; i >= 0; --i) {
            int p = position[i] + carry;
            if (p >= alphabet.length) {
                buf[offset + i] = alphabet[0];
                position[i] = 0;
            } else {
                carry = 0;
                buf[offset + i] = alphabet[p];
                position[i] = p;
                break;
            }
        }

        if (carry == 1) {
            return Optional.empty();
        }
        return Optional.of(buf);
    }
}
