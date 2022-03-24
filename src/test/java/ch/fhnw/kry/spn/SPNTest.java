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
}