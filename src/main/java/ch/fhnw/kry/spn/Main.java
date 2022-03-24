package ch.fhnw.kry.spn;

public class Main {

    public static void main(String[] args) {
        int[] sbox = {0xE, 0x4, 0xD, 0x1, 0x2, 0xF, 0xB, 0x8, 0x3, 0xA, 0x6, 0xC, 0x5, 0x9, 0x0, 0x7};
        int[] bitPermutations = {0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};

        SPN spn = new SPN(4, 4, 4, 32, 0b0011_1010_1001_0100_1101_0110_0011_1111, sbox, bitPermutations);


        String input = "000001001101001000001011101110000000001010001" +
                "111100011100111111101100000010100010100001110" +
                "10000000010011011001110010101110110000";

        System.out.println(spn.decrypt(input));
    }
}
