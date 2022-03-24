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
        var chunks = StringUtil.splitStringIntoChunks(plaintextMessage, n * m);

        var encrypted = new int[chunks.length];

        for (int i = 0; i < chunks.length; i++) {
            var tempResult = chunks[i];
            tempResult ^= roundKeys[0];

            for (int j = 1; j < r - 1; j++) {
                tempResult = applySBox(tempResult, this.sbox);
                tempResult = applyBitPermutation(tempResult, this.bitPermutations);
                tempResult ^= roundKeys[j];
            }
            tempResult = applySBox(tempResult, this.sbox);
            tempResult ^= roundKeys[r - 1];
            encrypted[i] = tempResult & 0xFFFF;
        }
        return mergeChunksIntoString(encrypted);
    }

    public String decrypt(String encryptedMessage) {
        var encrypted = StringUtil.splitStringIntoChunks(encryptedMessage, n * m);
        var decrypted = new int[encrypted.length];

        for (int i = 0; i < encrypted.length; i++) {
            var tempResult = encrypted[i];
            tempResult ^= roundKeys[r - 1];
            tempResult = applySBox(tempResult, this.invertedSBox);

            for (int j = r - 2; j > 0; j--) {
                tempResult ^= roundKeys[j];
                tempResult = applyBitPermutation(tempResult, this.bitPermutations);
                tempResult = applySBox(tempResult, this.invertedSBox);
            }
            tempResult ^= roundKeys[0];
            decrypted[i] = tempResult & 0xFFFF;
        }
        return mergeChunksIntoString(decrypted);
    }

    public String enDe(String plaintextMessage){
        var chunks = StringUtil.splitStringIntoChunks(plaintextMessage, n * m);

        var encripted = new int[chunks.length];

        for (int i = 0; i < chunks.length; i++) {
            var tempResult = chunks[i];
            tempResult ^= roundKeys[0];

            for (int j = 1; j < r - 1; j++) {
                tempResult = applySBox(tempResult, this.sbox);
                tempResult = applyBitPermutation(tempResult, this.bitPermutations);
                tempResult ^= roundKeys[j];
            }
            tempResult = applySBox(tempResult, this.sbox);
            tempResult ^= roundKeys[r - 1];
            encripted[i] = tempResult & 0xFFFF;
        }

        var decripted = new int[chunks.length];

        for (int i = 0; i < encripted.length; i++) {
            var tempResult = encripted[i];
            tempResult ^= roundKeys[r - 1];
            tempResult = applySBox(tempResult, this.invertedSBox);

            for (int j = r - 2; j > 0; j--) {
                tempResult ^= roundKeys[j];
                tempResult = applyBitPermutation(tempResult, this.bitPermutations);
                tempResult = applySBox(tempResult, this.invertedSBox);
            }
            tempResult ^= roundKeys[0];
            decripted[i] = tempResult & 0xFFFF;
        }
        return mergeChunksIntoString(decripted);
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
        var res = new byte[index];
        // If the amount of nimble is even, there is one nimble too much with value 0 at the end.
        System.arraycopy(bytes, 0, res, 0, index);
        return new String(res, StandardCharsets.UTF_8);
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
            var key = (input & (mask >>> i * 4)) >>> ((3 - i) * 4);
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
            var shiftTo = (mask >>> (list[i]));
            // Is the origin bit 1 or 0
            var maskValue = (input & (mask >>> i));
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
}
