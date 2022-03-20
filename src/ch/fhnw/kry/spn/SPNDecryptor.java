package ch.fhnw.kry.spn;

public class SPNDecryptor {

    private final int r;
    private final int n;
    private final int m;
    private final int s;

    private final int[] sbox;
    private final int[] bitPermutations;

    /**
     * Initializes the SPN Decryptor with the necessary parameters
     *
     * @param r
     * @param n
     * @param m
     * @param s
     * @param sbox
     * @param bitPermutations
     */
    public SPNDecryptor(int r, int n, int m, int s, int[] sbox, int[] bitPermutations) {
        this.r = r;
        this.n = n;
        this.m = m;
        this.s = s;
        this.sbox = sbox;
        this.bitPermutations = bitPermutations;
    }

    public String encrypt(String plaintextMessage) {
        return "encrypted message";
    }

    public String decrypt(String encryptedMessage) {
        return "decrypted message";
    }
}
