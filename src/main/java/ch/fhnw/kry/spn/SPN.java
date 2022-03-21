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


        var newChunks= new int[chunks.length];

        for (int i = 0; i < chunks.length; i++) {
            var tempResult=chunks[i];
            int initialRoundKey = roundKeys[0];

            tempResult ^= initialRoundKey;

            for (int j = 1; j < r - 1; j++) {
                tempResult= applySBox(tempResult, this.sbox);
                tempResult= applyBitPermutation(tempResult, this.bitPermutations);
                tempResult ^= roundKeys[j];
            }
            tempResult= applySBox(tempResult, this.sbox);
            tempResult ^= roundKeys[r-1];
            newChunks[i]=tempResult;
        }
        return mergeChunksIntoString(newChunks);
    }

    public String decrypt(String encryptedMessage) {
        return "decrypted message";
    }

    public static String mergeChunksIntoString(int[] chunks){
        var bytes=new byte[128];
        var index=0;
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





        var byteArray = input.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < chunks.length; i++) {
            int chunk = 0;
            for (int j = 0; j < 4; j++) {
                chunk += byteArray[i * 4 + j];
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
    private static int[] getRoundKeys(int k, int r) {
        // The length of the key must not be larger than 8 nimble
        assert k <= 0xFFFFFFFF;
        var res = new int[r];
        // Define the mask as 4 pairs of nimbles
        var mask = 0xFFFF;
        for (int i = 0; i < r; i++) {
            res[i] = k & (mask >> i * 4);
        }
        return res;
    }

    private static int applySBox(int input, int[] box){
        var mask=0b1111;
        var res=0;
        for (int i = 0; i < 4; i++) {
            // Extrapolate 1 nimble and shift it to the last place
            var key=(input & (mask >> i*4))>>3-i;
            // Get the box value
            var newValue=box[key];
            // Store back the result found
            res= res |(newValue<<3-i);
        }
        return res;
    }

    
    private static int applyBitPermutation(int input, int[] list){
        int res=0;
        int mask=0b1000_0000_0000_0000;
        for (int i = 0; i < list.length; i++) {
            res |= input & (mask >> (list[i]-1));
        }
        return res;
    }

    /**
     * Invert the sbox for decryption
     *
     * @param sbox
     * @return
     */
    private static int[] invertSBox(int[] sbox) {
        var res = new int[sbox.length];
        for (int i = 0; i < res.length; i++) {
            res[sbox[i]] = i;
        }
        return res;
    }
}
