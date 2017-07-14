package com.wirusmx.ole2editor.parsers;

import java.util.Arrays;

public class StreamSignature {
    private StreamSignatureEntry[] signature;

    public StreamSignature(StreamSignatureEntry[] signature) {
        this.signature = signature;
    }

    /**
     * @param bytes - stream bytes.
     * @return <code>true</code>if stream data matches to signature,
     * <code>false</code> - otherwise.
     */
    public boolean matchesTo(byte[] bytes) {
        for (StreamSignatureEntry e : signature) {
            if (e.getOffset() >= bytes.length) {
                return false;
            }

            for (int i = 0; i < e.getValue().length; i++) {
                if (e.getValue()[i] != bytes[e.getOffset() + i]) {
                    return false;
                }
            }

        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StreamSignature that = (StreamSignature) o;

        return Arrays.equals(signature, that.signature);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(signature);
    }
}
