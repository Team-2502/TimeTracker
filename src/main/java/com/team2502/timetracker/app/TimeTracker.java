package com.team2502.timetracker.app;

import com.team2502.timetracker.internal.JsonData;

import java.io.IOException;

@SuppressWarnings("All")
public class TimeTracker {

    public static void main(String[] args) throws IOException {
        JsonData dataFiles = new JsonData("data.json");
        App application = new App(dataFiles);
        application.show();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            application.destroy();
            try { dataFiles.store(); }
            catch(IOException e) { };
        }));
    }
}