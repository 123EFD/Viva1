import java.util.*;
import java.nio.charset.StandardCharsets;//get the UTF-8 Charset object

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the original message: ");
        String message = scanner.nextLine();

        // Use byte-based encoding (UTF-8-safe)
        //Converts the String to a byte[] using UTF-8 encoding
        List<Integer> encoded = encodeBytesWithBitwise(message.getBytes(StandardCharsets.UTF_8));

        System.out.println("Encoded message:");
        printSpaceSeparated(encoded);

        scanner.close();
    }

    // Encode an array of bytes using 8-bit inversion and return unsigned decimals 0..255
    //byte[]: sequence of 8-bit values (-128..127 signed)
    public static List<Integer> encodeBytesWithBitwise(byte[] bytes) {
        List<Integer> result = new ArrayList<>(bytes.length);
        for (byte b : bytes) {
             /*convert the signed byte to an unsigned i..255 int masking with 0xFF
              * The bitwise AND with 0xFF converts the byte to an int in the range 0..255 while preserving the original 8-bit bit pattern.
                ~unsigned computes the bitwise NOT of the 32-bit int.For example, if unsigned is 00000000 00000000 00000000 11111111 (255 in decimal),
                then ~unsigned becomes 11111111 11111111 11111111 00000000.
                &0xFF ensure only the 8-bit inverted value, not the negative 32-bit int
              */
            int unsigned = b & 0xFF;          
            int inverted = (~unsigned) & 0xFF; // invert and keep low 8 bits
            result.add(inverted);
        }
        return result;
    }

    // Print list of integers as space-separated values on one line, size() get the number of elements in the list
    private static void printSpaceSeparated(List<Integer> numbers) {
        for (int i = 0; i < numbers.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(numbers.get(i));
        }
        System.out.println();
    }
}
