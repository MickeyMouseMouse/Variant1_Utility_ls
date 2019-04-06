import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeMap;

public class Tests {
    // Для корректной работы тестов на Вашей машине, нужно задать актуальные входные данные!
    // ТЕСТЫ ЗАПУСКАТЬ ПО ОДНОМУ!
    @Test
    public void test1() {
        String[] args = {"/Users/AndJ/Documents/debugUtilityLs"};
        Ls.main(args);

        TreeMap<String, String> rightAnswer = new TreeMap<>();
        rightAnswer.put(".DS_Store", "");
        rightAnswer.put("1.png", "");
        rightAnswer.put("myFolder", "");
        assertEquals(rightAnswer, Ls.answer);
    }

    @Test
    public void test2() {
        String[] args = {"[-l]", "[-h]", "[-r]", "/Users/AndJ/Documents/debugUtilityLs"};
        Ls.main(args);

        TreeMap<String, String> rightAnswer = new TreeMap<>();
        rightAnswer.put("myFolder", "rwx  08:31  0B");
        rightAnswer.put("1.png", "rw-  08:32  402.38KB");
        rightAnswer.put(".DS_Store", "rw-  05:07  6KB");
        assertEquals(rightAnswer, Ls.answer);
    }

    @Test
    public void test3() {
        String[] args = {"/Users/AndJ/Documents/debugUtilityLs/myFolder"};
        Ls.main(args);

        TreeMap<String, String> rightAnswer = new TreeMap<>();
        rightAnswer.put("(empty directory)", "");
        assertEquals(rightAnswer, Ls.answer);
    }

    @Test
    public void test4() {
        String[] args = {"/Users/AndJ/Documents/debugUtilityLs/1.png"};
        Ls.main(args);

        TreeMap<String, String> rightAnswer = new TreeMap<>();
        rightAnswer.put("1.png", "11-  08:32  412038");
        assertEquals(rightAnswer, Ls.answer);
    }

    @Test
    public void test5() {
        String[] args = {"[-o", "1.txt]", "/Users/AndJ/Documents/debugUtilityLs/1.png"};
        Ls.main(args);

        String rightAnswer = "11-  08:32  412038   1.png";
        String fromFile = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("1.txt"));
            fromFile = reader.readLine();
        } catch (Exception e) {
            System.out.println("test 5 - runtime error");
        }

        assertEquals(rightAnswer, fromFile);
    }
}
