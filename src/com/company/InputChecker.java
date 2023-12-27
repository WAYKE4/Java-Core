package com.company;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputChecker {

    public List<File> inputCheck() throws InputException {
        String directoryPath = "C:\\Users\\User\\Desktop\\input";
        File directory = new File(directoryPath);

        boolean notExist;
        boolean emptyInput = false;
/**
 * Проверка на существование папки input .  Если нету , создает ее и просит закинуть входные файлы
 */
        if (!directory.exists()) {
            System.out.println("Input folder doesn't exist!");
            System.out.println("Creating input folder....");
            notExist = true;
            if (directory.mkdirs()) {
                System.out.println("input folder successfully created!");
            } else {
                System.out.println("input folder not created. System exit...");
                System.exit(5);
            }
            while (notExist) {
                System.out.println("Put some input files!");
                File[] files = directory.listFiles();
                if (files.length == 0) {
                    ;
                } else {
                    notExist = false;
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        File[] files = directory.listFiles();
        if (files == null) {
            throw new InputException("Path directory error!");
        }
        try {
            if (files.length == 0) {
                emptyInput = true;
                throw new InputException("Yours folder is empty!");
            }
        } catch (InputException e) {
            while (emptyInput) {
                System.out.println("Put some input files!");
                File[] files0 = directory.listFiles();
                if (files0.length == 0) {
                    ;
                } else {
                    emptyInput = false;
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ee) {
                    System.out.println(ee);
                }
            }
            files = directory.listFiles();
        }
        List<File> resultFiles = Arrays.stream(files)
                .filter((file) -> file.getName().endsWith(".txt"))
                .toList();
        if (resultFiles.isEmpty()) {
            throw new InputException("Yours folder doesn't have file in format .txt!");
        }
        return resultFiles;
    }

    /**
     * Проверка на валидность главных счетов(file-with-numbers) .  Если нет , игнорирует их в дальнейшем
     */
    public List<String> validTabChecker() {
        Pattern patternDataOut = Pattern.compile("^[\\d]{5}-[\\d]{5}:[\\d]+.+?$");
        Pattern patternDataOutCheck = Pattern.compile("^.*\\D$");
        ArrayList<String> arrOut = null;
        try (BufferedReader readerDataOut = new BufferedReader(new FileReader("C:\\Users\\User\\Desktop\\file-with-numbers.txt"))) {
            String dataOut;
            arrOut = new ArrayList<>();
            while ((dataOut = readerDataOut.readLine()) != null) {
                Matcher matcherDataOut = patternDataOut.matcher(dataOut);
                if (matcherDataOut.matches()) {
                    String dataOutCheck = dataOut.substring(dataOut.indexOf(":") + 1);
                    Matcher matcherDataOutChecker = patternDataOutCheck.matcher(dataOutCheck);
                    if (matcherDataOutChecker.matches()) {
                        System.out.println(dataOut + " will be ignored (Incorrect format)");
                        continue;
                    }
                    arrOut.add(dataOut);
                }
            }
            if (arrOut.isEmpty()) {
                System.out.println(("The entire file-with-numbers is in the wrong format!"));
                System.exit(15);
            }
        } catch (IOException e) {
            System.out.println("File-with-numbers doesn't exist!");
            System.exit(15);
        }

        File file = new File("C:\\Users\\User\\Desktop\\report-file.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\User\\Desktop\\report-file.txt"))) {
            if (!(file.length() == 0)) {
                PrintWriter writer = new PrintWriter(file);
                writer.print("");
                writer.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return arrOut;
    }

    /**
     * Метод принимает вычитанную строку из input-файлов и проверяет ее на правильность .
     * Если да - перезаписывает в dataList новое значение и так по циклу.
     * Если нет - обрабатывает исключение
     * В конце все этапы обработанных строк ( правильных и нет) записывает в report-file
     */
    public List<String> changingData(String correctOut, List<String> resultList, boolean isFirstTime) {
        List<String> dataList;
        if (isFirstTime) {
            dataList = validTabChecker();
        } else {
            dataList = resultList;
        }
        String newLine;
        for (int i = 0; i < Objects.requireNonNull(dataList).size(); i++) {
            try {
                String line = dataList.get(i);
                String endLine = line.substring(line.indexOf(":") + 1);
                String endCorrectLine = correctOut.substring(correctOut.indexOf(":") + 1);

                String lineBack = line.substring(0, line.indexOf(":") + 1);

                String beginLine = line.substring(0, line.indexOf(":"));
                String beginCorrectLine = correctOut.substring(0, correctOut.indexOf(":"));

                if (beginLine.equals(beginCorrectLine)) {
                    int a = Integer.parseInt(endLine);
                    int b = Integer.parseInt(endCorrectLine);
                    LocalDate localDate = LocalDate.now();
                    LocalTime localTime = LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
                    if (b < 0) {
                        try (BufferedWriter writerReport = new BufferedWriter(new FileWriter("C:\\Users\\User\\Desktop\\report-file.txt", true))) {
                            writerReport.write(localDate + " " + localTime + "||" + "перевод с " + beginCorrectLine + " на " + beginLine + ": " + endCorrectLine
                                    + " || ошибка: " + " Отрицательная сумма перевода" + "\n");
                            writerReport.flush();
                            continue;
                        } catch (IOException ee) {
                            System.out.println(ee);
                        }
                    }
                    int c = a + b;
                    newLine = lineBack + c;
                    dataList.set(i, newLine);
                    try (BufferedWriter writerReport = new BufferedWriter(new FileWriter("C:\\Users\\User\\Desktop\\report-file.txt", true))) {
                        writerReport.write(localDate + " " + localTime + "||" + "перевод с " + beginCorrectLine + " на " + beginLine + ": " + endCorrectLine
                                + " || успешно обработан \n");
                        writerReport.flush();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            } catch (NumberFormatException e) {
                try (BufferedWriter writerReport = new BufferedWriter(new FileWriter("C:\\Users\\User\\Desktop\\report-file.txt", true))) {
                    String line = dataList.get(i);
                    String endCorrectLine = correctOut.substring(correctOut.indexOf(":") + 1);
                    String beginLine = line.substring(0, line.indexOf(":"));
                    String beginCorrectLine = correctOut.substring(0, correctOut.indexOf(":"));
                    LocalDate localDate = LocalDate.now();
                    LocalTime localTime = LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
                    writerReport.write(localDate + " " + localTime + "||" + "перевод с " + beginCorrectLine + " на " + beginLine + ": " + endCorrectLine
                            + " || ошибка: Некорректные данные -" + e.getMessage().substring(17) + "\n");
                    writerReport.flush();
                } catch (IOException ee) {
                    System.out.println(ee);
                }
            }
        }
        return dataList;
    }

    /**
     * Метод перебирает у всех файлов , которые прошли проверку  на формат txt , их значения
     * Если находит строку , удольтворяющую нашему условию , посылает ее в метод changingData
     */
    public void regexChecker(List<File> resultFiles) {
        List<String> resultList = validTabChecker();
        System.out.println("DataIn: " + resultList);
        boolean isFirstTime = true;

        for (File resultFile : resultFiles) {
            try (BufferedReader readerFile = new BufferedReader(new FileReader(resultFile))) {
                Pattern patternData = Pattern.compile("^[\\d]{5}-[\\d]{5}:.*[\\d].*$");
                String line;

                while ((line = readerFile.readLine()) != null) {
                    Matcher matcherData = patternData.matcher(line);
                    if (matcherData.matches()) {
                        String correctOut = matcherData.group();
                        if (isFirstTime) {
                            resultList = changingData(correctOut, resultList, true);
                            isFirstTime = false;
                        } else {
                            resultList = changingData(correctOut, resultList, false);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        System.out.println("DataOut:" + resultList);
    }

    public void acrhiveWriter(List<File> resultFiles) {
        String directoryPath = "C:\\Users\\User\\Desktop\\archive";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            System.out.println("Archive doesn't exist!");
            System.out.println("Creating archive folder....");
            if (directory.mkdirs()) {
                System.out.println("archive folder successfully created!");
            } else {
                System.out.println("archive folder not created. System exit...");
                System.exit(5);
            }
        }

        for (File file : resultFiles) {
            try {
                File newFileInArchive = new File("C:\\Users\\User\\Desktop\\archive\\" + file.getName());
                Files.copy(file.toPath(), newFileInArchive.toPath());
                System.out.println(file.getName() + " successfully created in archive!");
            } catch (FileAlreadyExistsException e) {
                System.out.println("There are already files with the same name in the archive! " + e.getMessage().substring(30));
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}