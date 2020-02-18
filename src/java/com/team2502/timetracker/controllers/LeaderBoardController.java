package com.team2502.timetracker.controllers;

import com.team2502.timetracker.TimeTracker;
import com.team2502.timetracker.internal.JsonData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class LeaderBoardController implements Initializable
{
    @FXML
    Label name1;
    @FXML
    Label name2;
    @FXML
    Label name3;
    @FXML
    Label time1;
    @FXML
    Label time2;
    @FXML
    Label time3;

    @FXML
    AnchorPane anchorPane;

    @FXML
    AnchorPane medalPane;

    @FXML
    ScrollPane otherLeaderBoard;

    private JsonData jsonData;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    void init(){
        anchorPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            medalPane.setLayoutX(newValue.doubleValue()/2 - medalPane.getWidth()/2);
        });


        name1.setText(jsonData.getLeaderBoard()[0].getName());
        time1.setText("Hours: " +  String.format("%.2f", (double)jsonData.getLeaderBoard()[0].getTotalTime()/60));
        name2.setText(jsonData.getLeaderBoard()[1].getName());
        time2.setText("Hours: " +  String.format("%.2f", (double)jsonData.getLeaderBoard()[1].getTotalTime()/60));
        name3.setText(jsonData.getLeaderBoard()[2].getName());
        time3.setText("Hours: " +  String.format("%.2f", (double)jsonData.getLeaderBoard()[2].getTotalTime()/60));

        VBox vbox = new VBox();

        for(int i = 3; i < jsonData.getLeaderBoard().length; i++)
        {
            AnchorPane anchorPane = new AnchorPane();
            Label number = new Label(String.valueOf(i+1));
            Label name = new Label( jsonData.getLeaderBoard()[i].getName());
            Label time = new Label("Hours: " + String.format("%.2f", (double)jsonData.getLeaderBoard()[i].getTotalTime()/60));
            ImageView imageView = new ImageView(new Image(String.valueOf(TimeTracker.class.getResource("images/cup.png")), 25, 25, true, true));
            Separator separator =  new Separator();
            number.setFont(Font.font("Impact", 16));
            name.setFont(Font.font("Impact", 16));
            time.setFont(Font.font("Impact", 16));

            number.setBackground(new Background(new BackgroundFill(Paint.valueOf("Grey"), null, null)));
            number.setAlignment(Pos.CENTER);
            number.setPrefWidth(25);
            number.setPrefHeight(25);
            number.setLayoutX(10);
            number.setLayoutY(10);

            name.setAlignment(Pos.CENTER);
            name.setPrefHeight(25);
            name.setLayoutX(50);
            name.setLayoutY(10);

            time.setBackground(new Background(new BackgroundFill(Paint.valueOf("Grey"), null, null)));
            time.setAlignment(Pos.CENTER);
            time.setPrefWidth(100);
            time.setPrefHeight(25);
            time.setLayoutX(400);
            time.setLayoutY(10);

            imageView.setPreserveRatio(true);
            imageView.setLayoutY(10);
            imageView.setLayoutX(510);

            separator.setPrefWidth(otherLeaderBoard.getPrefWidth());
            separator.setPrefHeight(5);
            anchorPane.getChildren().add(number);
            anchorPane.getChildren().add(name);
            anchorPane.getChildren().add(time);
            anchorPane.getChildren().add(imageView);
            anchorPane.getChildren().add(separator);
            vbox.getChildren().add(anchorPane);
            vbox.setSpacing(10);

            otherLeaderBoard.widthProperty().addListener((observable, oldValue, newValue) -> {
                time.setLayoutX(newValue.doubleValue() - 175);
                imageView.setLayoutX(newValue.doubleValue() - 60);
                separator.setPrefWidth(otherLeaderBoard.getWidth());
            });
        }
        otherLeaderBoard.setContent(vbox);
    }

    void setJsonData(JsonData jsonData)
    {
        this.jsonData = jsonData;
    }
}
