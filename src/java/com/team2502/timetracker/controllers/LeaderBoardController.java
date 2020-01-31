package com.team2502.timetracker.controllers;

import com.team2502.timetracker.internal.JsonData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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
    ScrollPane otherLeaderBoard;

    private JsonData jsonData;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    void init(){
        name1.setText(jsonData.getLeaderBoard()[0].getName());
        time1.setText("Hours: " + (double)(jsonData.getLeaderBoard()[0].getTotalTime()/60));
        name2.setText(jsonData.getLeaderBoard()[1].getName());
        time2.setText("Hours: " + (double)(jsonData.getLeaderBoard()[1].getTotalTime()/60));
        name3.setText(jsonData.getLeaderBoard()[2].getName());
        time3.setText("Hours: " + (double)(jsonData.getLeaderBoard()[2].getTotalTime()/60));

        VBox vbox = new VBox();

        for(int i = 3; i < jsonData.getLeaderBoard().length; i++)
        {
            AnchorPane anchorPane = new AnchorPane();
            Label name = new Label(i + ". " + jsonData.getLeaderBoard()[i].getName());
            Label time = new Label("Hours: " + String.format("%.2f", (double)jsonData.getLeaderBoard()[i].getTotalTime()/60));
            name.setLayoutX(10);
            name.setLayoutY(10);
            time.setLayoutX(400);
            time.setLayoutY(10);
            anchorPane.getChildren().add(name);
            anchorPane.getChildren().add(time);
            vbox.getChildren().add(anchorPane);
            vbox.setSpacing(20);
        }
        otherLeaderBoard.setContent(vbox);
    }

    void setJsonData(JsonData jsonData)
    {
        this.jsonData = jsonData;
    }
}
