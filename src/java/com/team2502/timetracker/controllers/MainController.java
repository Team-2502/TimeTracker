package com.team2502.timetracker.controllers;

import com.team2502.timetracker.TimeTracker;
import com.team2502.timetracker.internal.JsonData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable
{
    @FXML
    AnchorPane anchorPane;

    @FXML
    ComboBox<String> dropDown;

    @FXML
    Button createUser;

    @FXML
    Button login;

    @FXML
    Button recalculateTotalTimes;

    private JsonData dataFiles;
    TextInputDialog dialog = new TextInputDialog("Ritik Mishra");

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        dialog.setTitle("Add User");
        dialog.setHeaderText("Use First And Last Names");
        dialog.setGraphic(new ImageView(new Image(String.valueOf(TimeTracker.class.getResource("images/TalonLogo.png")), 75, 75, true, true)));
        dialog.setContentText("Please enter your name:");

        try
        {
            dataFiles = new JsonData("data.json");
            dataFiles.store();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        for(String user : dataFiles.getUsers())
        {
            dropDown.getItems().add(user);
        }

        createUser.setOnAction(e -> {
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent())
            {
                String name = result.get();
                dataFiles.createUser(name);
                dropDown.getItems().add(name);
                dropDown.getSelectionModel().select(dropDown.getItems().size() - 1);
            }
        });

//       login.setText(dataFiles.userIsLoggedIn(dropDown.getValue()) ? "Logout" : "Login");
        login.setOnAction(e -> {
            dataFiles.toggleUserLogin(dropDown.getValue());
            login.setText(dataFiles.userIsLoggedIn(dropDown.getValue()) ? "Logout" : "Login");
            try {dataFiles.store();}
            catch(IOException f)
            {
                f.printStackTrace();
            }
        });


        dropDown.setOnAction(e -> {
            login.setText(dataFiles.userIsLoggedIn(dropDown.getValue()) ? "Logout" : "Login");
        });

//        recalculateTotalTimes.setOnAction(e -> dataFiles.recalculateTotalTimes());
//        anchorPane.getChildren().add(recalculateTotalTimes);
        anchorPane.requestFocus();
    }
}
