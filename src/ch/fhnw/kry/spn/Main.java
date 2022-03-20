package ch.fhnw.kry.spn;

public class Main {

    public static void main(String[] args) {
        int[] sbox = new int[16];

        sbox[0x0] = 0xE;
        sbox[0x1] = 0x4;
        sbox[0x2] = 0xD;
        sbox[0x3] = 0x1;
        sbox[0x4] = 0x2;
        sbox[0x5] = 0xF;
        sbox[0x6] = 0xB;
        sbox[0x7] = 0x8;
        sbox[0x8] = 0x3;
        sbox[0x9] = 0xA;
        sbox[0xA] = 0x6;
        sbox[0xB] = 0xC;
        sbox[0xC] = 0x5;
        sbox[0xD] = 0x9;
        sbox[0xE] = 0x0;
        sbox[0xF] = 0x7;

        int[] bitPermutations = new int[16];
        bitPermutations[0] = 0;
        bitPermutations[1] = 4;
        bitPermutations[2] = 8;
        bitPermutations[3] = 12;
        bitPermutations[4] = 1;
        bitPermutations[5] = 5;
        bitPermutations[6] = 9;
        bitPermutations[7] = 13;
        bitPermutations[8] = 2;
        bitPermutations[9] = 6;
        bitPermutations[10] = 10;
        bitPermutations[11] = 14;
        bitPermutations[12] = 3;
        bitPermutations[13] = 7;
        bitPermutations[14] = 11;
        bitPermutations[15] = 15;

        SPNDecryptor spnDecryptor = new SPNDecryptor(4, 4, 4, 32, sbox, bitPermutations);
    }
}
