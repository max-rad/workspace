package work;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        // поток для ввода данных с клавиатуры
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // Список, который содержит числа, [, ], буквы (слова)
        ArrayList<String> arrayList = new ArrayList<>();

        // Переменная для подсчета кол-ва [, ]
        int parentheses = 0;
        // Ввод строки с клавиатуры
        System.out.println("Input string");
        String input = reader.readLine();
        // Проверка на длину строки
        if (input.length() == 0) {
            throw new IllegalArgumentException();
        }

        // Проверка на специальные символы, кроме [, ]
        String regex = "[!\"#$%&'()*+,-./:;<=>?@^_`{|}~]";
        if (Pattern.compile(regex).matcher(input).find()) {
            throw new IllegalArgumentException();
        }
        // Поиск чисел, [, ], букв (слов), согласно заданному шаблону
        Pattern p = Pattern.compile("\\d+|[a-zA-Z]+|[\\[\\]]");
        //Matcher m = p.matcher(reader.readLine());
        Matcher m = p.matcher(input);
        while (m.find()) {
            if (m.group().equals("[") || m.group().equals(("]"))) {
                parentheses++;
            }
            arrayList.add(m.group());
            //System.out.println(m.group());
        }
        // Если количество символов [, ] нечетное, то кидаем исключение
        if (parentheses % 2 != 0) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < arrayList.size(); i++) {
            // Следует ли за числом символ [
            if (Pattern.matches("\\d+", arrayList.get(i))) {
                if (!arrayList.get(i + 1).equals("[")) {
                    throw new IllegalArgumentException();
                }
            }

            // Предшествует ли символу [ число
            if (arrayList.get(i).equals("[")) {
                // если символ [ стоит в начале
                if (i == 0) {
                    throw new IllegalArgumentException();
                }
                if (!Pattern.matches("\\d+", arrayList.get(i - 1))) {
                    throw new IllegalArgumentException();
                }
            }
        }

        // Список, содержащий числа
        ArrayList<Integer> numbers = new ArrayList<>();
        // Список, содержащий слова
        ArrayList<String> words = new ArrayList<>();

        // переменная для вычисления индекса последнего элемента списка в words
        int index;

        // Цикл по всему списку
        for (int i = 0; i < arrayList.size(); i++) {
            // Поиск чисел и запись в список
            if (Pattern.matches("\\d+", arrayList.get(i))) {
                numbers.add(Integer.parseInt(arrayList.get(i)));
            }

            // Поиск слов и запись их в список
            if (Pattern.matches("[a-zA-Z]+", arrayList.get(i))) {
                words.add(arrayList.get(i));
            }

            // Если встречается символ ], вычисляем индекс последнего элемента в списке words, повторяем этот элемент
            // нужно число раз и прибавляем его к предыдущему элемету, при этом удаляя последний (если число элементов
            // в списке words > 1)
            // xyz3[a]
            // xyz  -> xyzaaa
            // a    -> remove
            if (arrayList.get(i).equals("]")) {
                index = words.size() - 1;
                if (index > 0) {
                    words.set(index - 1, words.get(index - 1) + words.get(index).repeat(numbers.remove(numbers.size() - 1)));
                    words.remove(index);
                } else {
                    words.set(index, words.get(index).repeat(numbers.remove(numbers.size() - 1)));
                }
            }
        }
        // Вывод конечного результата
        System.out.println("Output string");
        for (String word : words) {
            System.out.print(word);
        }
        System.out.println();
    }
}
