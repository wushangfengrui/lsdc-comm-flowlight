import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TranspotTest {

    public static void main(String[] args){
        // $32$11$22
        // 001100100001000100100010
        System.out.println(Integer.valueOf("32",16));
        System.out.println(Integer.valueOf("11",16));
        System.out.println(Integer.valueOf("22",16));

        String erjinzhi = "001100100001000100100010";

        int a = 0b0101001;
        int b = 00101001;
        int c = 0x0101001;
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);

        Map<String,String> map = new HashMap<>();


    }

    static String toBinary(int num) {
        String str = "";
        while (num != 0) {
            str = num % 2 + str;
            num = num / 2;
        }
        return str;
    }
}
