import java.util.ArrayList;
import java.util.List;

/**
 * @author  Dmytro Sytnik (VanArman)
 * @version 15 January 2019
 */

public class TestBitParser {
    public TestBitParser() {
        byte[] d = new byte[]{0, 1, 2, 0, 122, 12, 22, 0, 23, 107, 17, 15, 0, 0};

        List<List> arr = new ArrayList<>();
        List<Byte> temp = new ArrayList<>();

        for(byte b : d) {
            if(b == (byte) 0) {
                if(temp.size() > 0) {
                    arr.add(temp);
                    temp = new ArrayList<>();
                }
                continue;
            }

            temp.add(Byte.valueOf(b));
        }

        for(int i = 0; i < arr.size(); i++) {
            System.out.println("\nArray: " +i);
            for(int j = 0; j < arr.get(i).size(); j++) {
                System.out.print(arr.get(i).get(j) +" ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        new TestBitParser();
    }
}
