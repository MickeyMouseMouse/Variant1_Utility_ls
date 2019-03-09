import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.TreeMap;

public class Tests {
    // Для корректной работы тестов на Вашей машине, нужно задать актуальные входные данные!
    // Тесты запускать по одному!
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
        rightAnswer.descendingMap();
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
        rightAnswer.put("1.png", "");
        assertEquals(rightAnswer, Ls.answer);
    }

    @Test
    public void test5() {
        String[] args = {"[-l]", "/Users/AndJ/Documents/debugUtilityLs/1.png"};
        Ls.main(args);

        TreeMap<String, String> rightAnswer = new TreeMap<>();
        rightAnswer.put("1.png", "11-  08:32  412038");
        assertEquals(rightAnswer, Ls.answer);
    }
}
