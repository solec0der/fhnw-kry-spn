package ch.fhnw.kry.spn;

import java.util.Random;

public class CTR {

    // maybe add an interface for SPN
    private final SPN spn;
    private final Random random = new Random();

    public CTR(SPN spn) {
        this.spn = spn;
    }

    public String encrypt(String plaintextMessage) {
        var nonce = getNonce();
        var nonceBin = StringUtil.splitStringIntoChunks(nonce, 16)[0]; // TODO: Replace chunkSize with var from SPN

        var blocks = StringUtil.splitStringIntoChunks(plaintextMessage, 16);

        for (int i = 0; i < blocks.length; i++) {
            var currentNonce = nonceBin + i;
            var encryptedNonce = StringUtil.convertStringToBinary(spn.encrypt(Integer.toBinaryString(currentNonce)));
            var encryptedNonceBin = StringUtil.splitStringIntoChunks(encryptedNonce, 16)[0]; // TODO: Replace chunkSize with var from SPN

            blocks[i] ^= encryptedNonceBin;
        }
        return nonce + StringUtil.convertStringToBinary(SPN.mergeChunksIntoString(blocks));
    }

    public String decrypt(String encryptedMessage) {
        return "";
    }


    private String getNonce() {
        var upperBound = 0xFFFF;
        var lowerBound = 0xAAAA;

        var nonce = random.nextInt(upperBound - lowerBound) + lowerBound;

        return Integer.toBinaryString(nonce & 0xFFFF);
    }
}
