package com.team2502.timetracker.internal;

import org.json.JSONObject;

import java.io.*;

@SuppressWarnings("All")
public class JsonStorage {

    public static void writeJsonToFile(JSONObject json, String path) throws IOException {
        try(FileWriter writer = new FileWriter(path)) {
            writer.write(json.toString(3));
        }catch(IOException e) { throw e; }
    }

    public static JSONObject readJsonFromFile(String path) throws IOException {
        final StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine();
            while(line != null) {
                sb.append(line);
                line = reader.readLine();
            }
        }catch(IOException e) { throw e; }

        return new JSONObject(new String(sb));
    }
}