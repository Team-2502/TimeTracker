package com.team2502.timetracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TimeTracker extends Application {

    public static Stage leaderBoardStage;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent mainParent = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
//        Parent leaderBoardParent = FXMLLoader.load(getClass().getResource("fxml/leaderBoard.fxml"));
        Scene mainScene = new Scene(mainParent);
//        Scene leaderBoardScene = new Scene(leaderBoardParent);
        primaryStage.setTitle("Time Tracker");
//        leaderBoardStage.setTitle("Leader Board");
        primaryStage.setScene(mainScene);
//        leaderBoardStage.setScene(leaderBoardScene);
        primaryStage.setResizable(false);
//        leaderBoardStage.setResizable(false);
        primaryStage.show();
    }
}