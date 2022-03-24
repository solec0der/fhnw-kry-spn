package ch.fhnw.kry.spn;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class SPN {

    private final int r;
    private final int n;
    private final int m;
    private final int s;
    private final int k;

    private final int[] sbox;
    private final int[] invertedSBox;
    private final int[] bitPermutations;

    private final int[] roundKeys;

    /**
     * Initializes the SPN Decryptor with the necessary parameters
     *
     * @param r
     * @param n
     * @param m
     * @param s
     * @param sbox
     * @param bitPermutations
     * @param k
     */
    public SPN(int r, int n, int m, int s, int k, int[] sbox, int[] bitPermutations) {
        this.r = r;
        this.n = n;
        this.m = m;
        this.s = s;
        this.k = k;
        this.sbox = sbox;
        this.invertedSBox = invertSBox(sbox);
        this.bitPermutations = bitPermutations;

        this.roundKeys = getRoundKeys(k, r);
    }

    public String encrypt(String plaintextMessage) {
        var chunks = splitStringIntoChunks(plaintextMessage, n * m);

        var newChunks = new int[chunks.length];

        for (int i = 0; i < chunks.length; i++) {
            var tempResult = chunks[i];
            int initialRoundKey = roundKeys[0];

            tempResult ^= initialRoundKey;

            for (int j = 1; j < r - 1; j++) {
                tempResult = applySBox(tempResult, this.sbox);
                tempResult = applyBitPermutation(tempResult, this.bitPermutations);
                tempResult ^= roundKeys[j];
            }
            tempResult = applySBox(tempResult, this.sbox);
            tempResult ^= roundKeys[r - 1];
            newChunks[i] = tempResult;
        }
        return mergeChunksIntoString(newChunks);
    }

    public String decrypt(String encryptedMessage) {
        return "decrypted message";
    }

    public static String mergeChunksIntoString(int[] chunks) {
        var bytes = new byte[128];
        var index = 0;
        for (int chunk : chunks) {
            var b = BigInteger.valueOf(chunk).toByteArray();
            for (byte aByte : b) {
                bytes[index++] = aByte;
            }
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static int[] splitStringIntoChunks(String input, int chunkSize) {
        var chunks = new int[input.length() / chunkSize];
        var byteArray = extractBytesFromString(input);

        for (int i = 0; i < chunks.length; i++) {
            int chunk = 0;
            for (int j = 0; j < 2; j++) {
                chunk += byteArray[i * 2 + j];
            }
            chunks[i] = chunk;
        }

        return chunks;
    }

    /**
     * Given a key k, generate r different elements
     *
     * @param k
     * @param r
     * @return
     */
    public static int[] getRoundKeys(long k, int r) {
        // The length of the key must not be larger than 8 nimble
        assert k <= 0xFFFFFFFFL;
        var res = new int[r];
        // Define the mask as 4 pairs of nimble
        var mask = 0xFFFF0000;
        for (int i = 0; i < r; i++) {
            var position = (mask >>> i * 4);
            var chunk = (((int) k) & position);
            res[i] = chunk >> ((r - i - 1) * 4);
        }
        return res;
    }

    public static int applySBox(int input, int[] box) {
        var mask = 0b1111_0000_0000_0000;
        var res = 0;
        for (int i = 0; i < 4; i++) {
            // Extrapolate 1 nimble and shift it to the last place
            var key = (input & (mask >> i * 4)) >> ((3 - i) * 4);
            // Get the box value
            var newValue = box[key];
            // Store back the result found
            res = res | (newValue << ((3 - i) * 4));
        }
        return res;
    }

    public static int applyBitPermutation(int input, int[] list) {
        int res = 0;
        int mask = 0b1000_0000_0000_0000;
        for (int i = 0; i < list.length; i++) {
            // Get goal bit pointer
            var shiftTo = (mask >> (list[i]));
            // Is the origin bit 1 or 0
            var maskValue = (input & (mask >> i));
            if (maskValue > 0) {
                // If the origin is 1, put to the goal a 1
                res |= shiftTo & Integer.MAX_VALUE;
            }
        }
        return res;
    }

    /**
     * Invert the sbox for decryption
     *
     * @param sbox
     * @return
     */
    public static int[] invertSBox(int[] sbox) {
        var res = new int[sbox.length];
        for (int i = 0; i < res.length; i++) {
            res[sbox[i]] = i;
        }
        return res;
    }

    private static byte[] extractBytesFromString(String input) {
        byte[] bytes = new byte[input.length() / 8];

        for (int i = 0; i < input.length() / 8; i++) {
            byte parsedInput = (byte) (Integer.parseInt(input.substring(i * 8, i * 8 + 8), 2) & 0xFF);

            bytes[i] = parsedInput;
        }

        return bytes;
    }
}
