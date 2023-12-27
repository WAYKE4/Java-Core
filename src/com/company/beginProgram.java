package com.company;

import java.io.*;
import java.util.Scanner;

/**
 * Метод запускает switch на парсинг ; файл-отчет ; выход
 */
public class beginProgram {

    public void beginParsing() throws InputException {
        boolean parsing = true;
        String directoryPath = "C:\\Users\\User\\Desktop\\report-file.txt";
        File directory = new File(directoryPath);
        try {
            if (directory.createNewFile()) {
                System.out.println("Report-file successfully created!");
            }
        } catch (IOException eeq) {
            System.out.println(eeq);
        }

        try (BufferedWriter writerReport = new BufferedWriter(new FileWriter(directory))) {
            String[] files = directory.list();
            if ((files == null)) {
                directory.delete();
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        System.out.println("1. Parsing");
        System.out.println("2. File-report");
        System.out.println("3. Exit");

        boolean firstTimeParsing = true;
        while (parsing) {
            Scanner scanner = new Scanner(System.in);
            int num = scanner.nextInt();
            switch (num) {
                case 1 -> {
                    InputChecker inputChecker = new InputChecker();
                    inputChecker.regexChecker(inputChecker.inputCheck());
                    firstTimeParsing = false;
                }
                case 2 -> {
                    File file = new File("C:\\Users\\User\\Desktop\\report-file.txt");
                    try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\User\\Desktop\\report-file.txt"))) {
                        if (file.exists() && firstTimeParsing) {
                            PrintWriter writer = new PrintWriter(file);
                            writer.print("");
                            writer.close();
                            System.out.println("You not parsing!!");
                        }
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line + "\n");
                        }
                        firstTimeParsing = true;
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
                case 3 -> {
                    InputChecker inputChecker = new InputChecker();
                    inputChecker.acrhiveWriter(inputChecker.inputCheck());
                    System.exit(0);
                }
                default -> {
                    System.out.println("Uncorrect number , try again!");
                    System.out.println("1. Parsing");
                    System.out.println("2. File-report");
                    System.out.println("3. Exit");
                }
            }
        }
    }
}
