package com.team2502.timetracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TimeTracker extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("Time Tracker");
        Parent mainParent = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
        Scene mainScene = new Scene(new Group(mainParent), 600, 400);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
