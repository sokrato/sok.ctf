package io.github.sokrato.util;

import java.util.Objects;
import java.util.function.Predicate;

public class ChoiceWalker {
    private final byte[] choices;

    public ChoiceWalker() {
        this(Text.ALPHA_NUM_PUNC);
    }

    public ChoiceWalker(String choices) {
        this(choices.getBytes());
    }

    public ChoiceWalker(byte[] choices) {
        this.choices = Objects.requireNonNull(choices);
        if (this.choices.length == 0) {
            throw new IllegalArgumentException();
        }
    }

    public byte[] walk(byte[] buf, Predicate<byte[]> pred) {
        return walk(buf, 0, buf.length, pred);
    }

    public byte[] walk(byte[] buf, int offset, int len, Predicate<byte[]> pred) {
        for (int i = 0; i < len; ++i) {
            buf[offset + i] = choices[0];
        }

        int[] indices = new int[len];
        indices[len - 1] = -1;
        byte[] res = null;

        while (true) {
            int carry = 1;
            // find next
            for (int i = indices.length - 1; i >= 0; --i) {
                int p = indices[i] + carry;
                if (p >= choices.length) {
                    buf[offset + i] = choices[0];
                    indices[i] = 0;
                } else {
                    carry = 0;
                    buf[offset + i] = choices[p];
                    indices[i] = p;
                    break;
                }
            }

            // no more
            if (carry > 0) {
                break;
            }

            // hit
            if (pred.test(buf)) {
                res = buf;
                break;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        ChoiceWalker cw = new ChoiceWalker("abc".getBytes());
        byte[] res = cw.walk(new byte[2], bs -> {
            System.out.println(new String(bs));
            return false;
        });
        if (res != null) {
            System.out.println(new String(res));
        }
    }
}
