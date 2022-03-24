package ch.fhnw.kry.spn;

public class StringUtil {

    private static final int BITS_IN_BYTE = 8;

    private StringUtil() {
    }

    public static int[] splitStringIntoChunks(String input, int chunkSize) {
        var inputLength = input.length() + input.length() % chunkSize;

        var chunks = new int[inputLength / chunkSize];
        var bytes = StringUtil.extractBytesFromString(input);

        var bytesPerChunk = chunkSize / BITS_IN_BYTE;

        for (int i = 0; i <= (bytes.length / bytesPerChunk); i++) {
            var chunk = 0;

            for (int j = 0; j < bytesPerChunk; j++) {
                var shift = BITS_IN_BYTE * (bytesPerChunk - j - 1);
                var mask = 0xFF << (BITS_IN_BYTE * (bytesPerChunk - j - 1));

                int index = Math.min(i * bytesPerChunk + j, bytes.length);

                if (index < bytes.length) {
                    chunk = chunk | ((bytes[index] << shift) & mask);
                } else {
                    chunk = chunk >> 8;
                }
            }
            if (i < chunks.length) {
                chunks[i] = chunk;
            }
        }
        return chunks;
    }

    public static byte[] extractBytesFromString(String input) {
        byte[] bytes = new byte[input.length() / 8];

        for (int i = 0; i < input.length() / 8; i++) {
            byte parsedInput = (byte) (Integer.parseInt(input.substring(i * 8, i * 8 + 8), 2) & 0xFF);

            bytes[i] = parsedInput;
        }

        return bytes;
    }

    public static String convertStringToBinary(String input) {
        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(aChar))   // char -> int, auto-cast
                            .replaceAll(" ", "0")                         // zero pads
            );
        }
        return result.toString();
    }
}
