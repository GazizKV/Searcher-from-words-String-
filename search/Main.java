package search;

import java.io.*;
import java.util.*;

public class Main {

    public static ArrayList<String> data = new ArrayList<>();
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static final Map<String, Set<Integer>> indexes = new HashMap<>();

    public static void main(String[] args) {

        /**
         * Чтение имени файла из аргументов
         * */
        String fileName = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--data")) {
                fileName = args[i + 1];
                break;
            }
        }

        /**
         * Чтение из файла
         * */
        try (
                BufferedReader fileReader = new BufferedReader(new FileReader(
                        "C:\\Users\\valit\\IdeaProjects\\Simple Search Engine\\Simple Search Engine\\task\\"
                                + fileName
                ))
        ) {
            while (fileReader.ready()) {
                data.add(fileReader.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        /**
         * Индексирование
         * */
        indexes();

        while (true) {
            System.out.println("=== Menu ===\n" +
                    "1. Search information.\n" +
                    "2. Print all data.\n" +
                    "0. Exit.\n");
            String inputLine = readLine();
            switch (inputLine) {
                case "1":
                    find();
                    break;
                case "2":
                    listOfPeople();
                    break;
                case "0":
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.println("Incorrect option! Try again.");
                    break;
            }
        }
    }

    private static void indexes() {
        for (String nextLine : data) {
            for (String nextWord : nextLine.split(" ")) {
                indexes.put(nextWord.toLowerCase(), new HashSet<>());
            }
        }
        for (String nextWord : indexes.keySet()) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).toLowerCase().contains(nextWord.toLowerCase())) {
                    indexes.get(nextWord).add(i);
                }
            }
        }
    }

    private static void listOfPeople() {
        System.out.println("=== List of people ===");
        for (String nextString : data) {
            System.out.println(nextString);
        }
    }

    private static void find() {
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String matchingStrategy = readLine();
        System.out.println("Enter a name or email to wordToFind all suitable people.");
        String wordToFind = readLine();
        switch (matchingStrategy) {
            case "ALL":
                findAll(wordToFind);
                break;
            case "ANY":
                findAny(wordToFind);
                break;
            case "NONE":
                findNON(wordToFind);
                break;
            default:
                break;
        }
    }

    private static void findNON(String wordToFind) {
        String[] words = wordToFind.split(" ");
        Set<Integer> indexesToOutput = new HashSet<>();

        for (String nextWord : words) {
            if (indexes.containsKey(nextWord.toLowerCase())) {
                indexesToOutput.addAll(indexes.get(nextWord.toLowerCase()));
            }
        }

        if (!(indexesToOutput.size() == data.size())) {
            System.out.println(data.size() - indexesToOutput.size() + "persons found:");
            for (int i = 0; i < data.size(); i++) {
                if (!indexesToOutput.contains(i)) {
                    System.out.println(data.get(i));
                }
            }
        } else {
            System.out.println("No matching people found.");
        }
    }

    private static void findAny(String wordToFind) {
        String[] words = wordToFind.split(" ");
        Set<Integer> indexesToOutput = new HashSet<>();

        for (String nextWord : words) {
            if (indexes.containsKey(nextWord.toLowerCase())) {
                indexesToOutput.addAll(indexes.get(nextWord.toLowerCase()));
            }
        }

        if (indexesToOutput.size() > 0) {
            System.out.println(indexesToOutput.size() + "persons found:");
            indexesToOutput.forEach(nextInt -> {
                System.out.println(data.get(nextInt));
            });
        } else {
            System.out.println("No matching people found.");
        }
    }

    private static void findAll(String wordToFind) {
        String[] words = wordToFind.split(" ");
        List<String> dataToOutput = new ArrayList<>();
        for (String nextLine : data) {
            if (matchingAll(words, nextLine)) {
                dataToOutput.add(nextLine);
            }
        }
        if (dataToOutput.size() > 0) {
            System.out.println(dataToOutput.size() + " persons found:");
            dataToOutput.forEach(System.out::println);
        } else {
            System.out.println("No matching people found.");
        }
    }

    private static boolean matchingAll(String[] words, String nextLine) {
        for (String nextString : words) {
            if (!nextLine.toLowerCase().contains(nextString.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public static String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
