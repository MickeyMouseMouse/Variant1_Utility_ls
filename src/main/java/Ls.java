import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/*
Вариант 1 - ls

Вывод содержимого указанной в качестве аргумента директории в виде отсортированного списка имен файлов.

Флаг -l (long) переключает вывод в длинный формат, в котором, кроме имени файла,
указываются права на чтение/запись/выполнение в виде битовой маски XXX,
время последней модификации и размер в байтах.

Флаг -h (human-readable) переключает вывод в человеко-читаемый формат
(размер в кило-, мега- или гигабайтах, права на выполнение в виде rwx).

Флаг -r (reverse) меняет порядок вывода на противоположный.

Флаг -o (output) указывает имя файла, в который следует вывести результат;
если этот флаг отсутствует, результат выводится в консоль.

В случае, если в качестве аргумента указан файл, а не директория, следует вывести информацию об этом файле.

Command Line: ls [-l] [-h] [-r] [-o output.file] directory_or_file
*/

public class Ls {
    private static File item; // Указанная директория или файл

    private static boolean flL = false; // Наличие/отсутствие флага [-l]
    private static boolean flH = false; // Наличие/отсутствие флага [-h]
    private static boolean flR = false; // Наличие/отсутствие флага [-r]
    private static String outputPath = ""; // Файл для вывода (флаг [-o outputPath])

    // Результирующий ассоциативный массив по возрастанию:
    // ключ - имя поддиректории или файла, значение - дополнительная информация
    // (private отсутствует для возможности прогона тестов)
    static TreeMap<String, String> answer = new TreeMap<>();

    public static void main(String[] args) {
        // Проверка на отсутствие аргументов
        if (args.length == 0) {
            System.out.println("Example: [-l] [-h] [-r] [-o output.txt] directory_or_file");
            return;
        }

        // По условию задания предполагается, что последним аргументом обязательно
        // должен быть путь к директории или файлу
        item = new File(args[args.length - 1]);

        // Проверка на отсутствие указанной дириктории или файла
        if (!item.exists()) {
            System.out.println(args[args.length - 1] + " - doesn't exist");
            return;
        }

        // Анализ флагов (наличие/отсутствие/корректность)
        getFlags(args);

        // Заполнение ключей answer именами поддиректорий и/или файлов
        if (item.isDirectory()) {
            String[] names = item.list();
            if (names == null) {
                System.out.println("Runtime error");
                return;
            }
            if (names.length == 0) {
                answer.put("(empty directory)", "");
                flL = false;
                printAnswer();
                return;
            }

            for (String str : names)
                answer.put(str, "");
        }
        else
            answer.put(item.getName(), "");

        // Добавление информации про права, время последней модификации
        // и размера директории или файла
        if (flL)
            if (flH)
                getMoreInfo(true); // [-l] [-h]
            else
                getMoreInfo(false); // [-l]

        // Реверс при наличии флага -r
        if (flR) answer = new TreeMap<>(answer.descendingMap());

        // Вывод результата
        printAnswer();
    }

    // Функция для анализа флагов (наличие/отсутствие/корректность)
    private static void getFlags(String[] args) {
        // Если указан файл, то (по условию) информация о нем выводится автоматически
        if (item.isFile()) flL = true;

        int i = 0;
        while (i != args.length - 1) {
            if (args[i].equals("[-l]")) {
                flL = true;
                i++;
                continue;
            }

            if (args[i].equals("[-h]")) {
                flH = true;
                i++;
                continue;
            }

            if (args[i].equals("[-r]")) {
                flR = true;
                i++;
                continue;
            }

            if (args[i].equals("[-o")) {
                outputPath = args[++i].substring(args[i].length() - 1);
                i++;
                continue;
            }

            System.out.println(args[i] + " - unknown flag");
            System.out.println("Example: [-l] [-h] [-r] [-o output.txt] directory_or_file");
            System.exit(0);
        }
    }

    // Функция для добавления информации про права, время последней модификации
    // и размера директории или файла
    // Если есть [-l] и [-h], mode = true
    private static void getMoreInfo(boolean mode) {
        File[] array = {item};
        if (item.isDirectory()) {
            array = item.listFiles();
            if (array == null) return;
        }

        for (File f : array) {
            StringBuilder str = new StringBuilder();

            // Права
            if (f.canRead())
                if (mode)
                    str.append("r");
                else
                    str.append("1");
            else
                str.append("-");

            if (f.canWrite())
                if (mode)
                    str.append("w");
                else
                    str.append("1");
            else
                str.append("-");

            if (f.canExecute())
                if (mode)
                    str.append("x");
                else
                    str.append("1");
            else
                str.append("-");

            // Время последней модификации (часы:минуты)
            str.append("  ")
               .append(new SimpleDateFormat("hh:mm")
                       .format((new Date(f.lastModified()))))
               .append("  ");

            // Размер
            long lengthItemInBytes;
            if (f.isDirectory())
                lengthItemInBytes = getDirectorySize(f);
            else
                lengthItemInBytes = f.length();

            if (mode)
                addHumanReadableLength(str, lengthItemInBytes);
            else
                str.append(lengthItemInBytes);

            answer.replace(f.getName(), str.toString());
        }
    }

    // Вычисление размера директории
    private static long getDirectorySize(File item) {
        long result = 0;
        File[] files = item.listFiles();

        if (files == null) return 0;
        for (File f : files)
            if (f.isFile())
                result += f.length();
            else
                result += getDirectorySize(f);

        return result;
    }


    // Преобразование размера файла из байт в кило-, мега- или гигабайты
    private static void addHumanReadableLength(StringBuilder str, long lengthItemInBytes) {
        final double forGB = 1024 * 1024 * 1024;
        final double forMB = 1024 * 1024;
        final double forKB = 1024;

        DecimalFormat lengthItem = new DecimalFormat("###.##"); // Шаблон для вывода дробных чисел
        if (lengthItemInBytes >= forGB)
            str.append(lengthItem.format(lengthItemInBytes / forGB)).append("GB");
        else if (lengthItemInBytes >= forMB)
            str.append(lengthItem.format(lengthItemInBytes / forMB)).append("MB");
        else if (lengthItemInBytes >= forKB)
            str.append(lengthItem.format(lengthItemInBytes / forKB)).append("KB");
        else str.append(lengthItemInBytes).append("B");
    }

    // Функция для вывода результата
    private static void printAnswer() {
        // Вывод в консоль
        if (outputPath.isEmpty()) {
            answer.forEach((k,v) -> {
                if (flL)
                    System.out.println(v + "   " + k);
                else
                    System.out.println(k);
            });
        } else {
            // Вывод в указанный файл (при наличии флага [-o 1.txt])
            try
            {
                FileWriter outputFile = new FileWriter(outputPath);

                for (Map.Entry<String, String> map : answer.entrySet())
                    if (flL)
                        outputFile.write(map.getValue() + "   " + map.getKey() + "\n");
                    else
                        outputFile.write(map.getKey() + "\n");

                outputFile.close();
            }
            catch (IOException e)
            {
                System.out.println("Failed to create output file: " + outputPath);
            }
        }
    }
}
