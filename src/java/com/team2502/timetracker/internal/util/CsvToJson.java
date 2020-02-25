package com.team2502.timetracker.internal.util;

import com.team2502.timetracker.internal.JsonData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvToJson {

    public static void main(String[] args) throws IOException {
        File directory = new File("csv");
        List<String> csvFiles;
        try (Stream<Path> paths = Files.walk(Paths.get(directory.getAbsolutePath()))) {
            csvFiles = paths.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
        }catch(IOException e) {
            e.printStackTrace();
            return;
        }

        JsonData data = new JsonData("data.json");
        for(String file : csvFiles) {
            try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String date = reader.readLine().replaceAll(",", "");
                reader.readLine();

                String line = reader.readLine();
                while(line != null) {
                    String[] parts = Arrays.copyOfRange(line.split(","), 0, 3);
                    line = reader.readLine();
                    if(parts[0] == null)
                        continue;

                    parts[0] = parts[0].trim();
                    if(parts[0].equals("") || parts[0].equals("Name"))
                        continue;

                    if(!data.userExists(parts[0])) data.createUser(parts[0]);
                    try { data.addTimeData(parts[0], parse(date, parts[1]), parse(date, parts[2])); }
                    catch(Exception e) { /* Ignore error */ }
                }
            }
        }

        data.recalculateTotalTimes();
        data.store();

        System.out.println("Transferred data from csv files to json");
    }

    private static LocalDateTime parse(String date, String time) {
        time = time.split(":")[0].length() == 1 ? "0"+time : time;
        LocalDate localDate  = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm a"));
        return LocalDateTime.of(localDate, localTime);
    }
}