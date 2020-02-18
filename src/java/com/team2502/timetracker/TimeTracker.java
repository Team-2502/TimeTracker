package com.team2502.timetracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TimeTracker extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent mainParent = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
        Scene mainScene = new Scene(mainParent);

        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(620);
        primaryStage.setTitle("Time Tracker");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}