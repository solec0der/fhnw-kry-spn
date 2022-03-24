package ch.fhnw.kry.spn;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SPNTest {
    @Test
    void testApplyBitPermutationInvert() {
        var perm = new int[]{0x0F, 0x0E, 0x0D, 0x0C, 0x0B, 0x0A, 0x09, 0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};
        var input = 0b1010_0101_0000_1111;
        var exp = 0b1111_0000_1010_0101;

        var res = SPN.applyBitPermutation(input, perm);


        assertEquals(Integer.toBinaryString(exp), Integer.toBinaryString(res));
    }

    @Test
    void testApplySBox() {
        var perm = new int[]{0x0F, 0x0E, 0x0D, 0x0C, 0x0B, 0x0A, 0x09, 0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};
        var input = 0b1000_0001_0010_1111;
        var exp = 0b0111_1110_1101_0000;

        var res = SPN.applySBox(input, perm);


        assertEquals(Integer.toBinaryString(exp), Integer.toBinaryString(res));
    }

    @Test
    void testInvertSBox() {
        var box = new int[]{0x0C, 0x0A, 0x0D, 0x0F, 0x0B, 0x0E, 0x07, 0x08, 0x09, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};

        var inv = SPN.invertSBox(box);
        assertNotEquals(box[0], inv[0]);
        var restore = SPN.invertSBox(inv);
        assertArrayEquals(box, restore);
    }

    @Test
    void testGetRoundKeys() {
        int k = 0b0001_1010_1111_1100_0000_0011_0101_0000;
        var expected = new int[]{
                0b0001_1010_1111_1100,
                0b1010_1111_1100_0000,
                0b1111_1100_0000_0011,
                0b1100_0000_0011_0101,
                0b0000_0011_0101_0000,
        };

        var res = SPN.getRoundKeys(k, 5);
        assertArrayEquals(expected, res);
    }

    @Test
    void testMergeChunksIntoStringOdd() {
        var exp = "Hello";
        var chunks = new int[]{
                0b0100_1000, 0b0110_0101_0110_1100, 0b0110_1100_0110_1111
        };
        var result = SPN.mergeChunksIntoString(chunks);
        assertEquals(exp, result);
    }

    @Test
    void testMergeChunksIntoStringEven() {
        var exp = ".Hello";
        var chunks = new int[]{
                0b0010_1110_0100_1000, 0b0110_0101_0110_1100, 0b0110_1100_0110_1111
        };
        var result = SPN.mergeChunksIntoString(chunks);
        assertEquals(exp, result);
    }

    @Test
    void testSplitAndMergeChunks() {
        var input = "Hello there";
        var r1 = SPN.splitStringIntoChunks(SPN.convertStringToBinary(input), 16);
        var result = SPN.mergeChunksIntoString(r1);
        assertEquals(input, result);
    }

    @Test
    void testEncryptDecrypt() {
        var input = "Hello there";
        var inputBin = "0100100001100101011011000110110001101111001000000111010001101000011001010111001001100101";
        int[] sbox = {0xE, 0x4, 0xD, 0x1, 0x2, 0xF, 0xB, 0x8, 0x3, 0xA, 0x6, 0xC, 0x5, 0x9, 0x0, 0x7};
        int[] bitPermutations = {0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};

        SPN spn = new SPN(4, 4, 4, 32, 0b0011_1010_1001_0100_1101_0110_0011_1111, sbox, bitPermutations);
        var en = spn.encrypt(inputBin);
        var res = spn.decrypt(spn.convertStringToBinary(en));
        assertEquals(input, res);
    }
}