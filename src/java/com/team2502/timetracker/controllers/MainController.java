package com.team2502.timetracker.controllers;

import com.team2502.timetracker.TimeTracker;
import com.team2502.timetracker.internal.JsonData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

@SuppressWarnings("All")
public class MainController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private ComboBox<String> dropDown;

    @FXML
    private Button login;

    @FXML
    private Button createUser;

    private JsonData dataFiles;
    private TextInputDialog dialog = new TextInputDialog();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try { dataFiles = new JsonData("data.json"); }
        catch(Exception e) { errorPopup(e); }

        // TODO: actions for menu
        Menu home = menuBar.getMenus().get(0);
        Menu leaderboard = menuBar.getMenus().get(1);
        Menu settings = menuBar.getMenus().get(2);

        home.setOnAction(e -> System.out.println("Home"));
        leaderboard.setOnAction(e -> System.out.println("Leaderboard"));
        settings.setOnAction(e -> System.out.println("Settings"));

        dropDown.getItems().addAll(Arrays.asList(dataFiles.getUsers()));
        dropDown.setOnAction(e -> login.setText(dataFiles.userIsLoggedIn(dropDown.getValue()) ? "Logout" : "Login"));
        if(!dropDown.getItems().isEmpty())
            dropDown.getSelectionModel().select(0);

        login.setText(dataFiles.userIsLoggedIn(dropDown.getValue()) ? "Logout" : "Login");
        login.setOnAction(e -> {
            dataFiles.toggleUserLogin(dropDown.getValue());
            login.setText(dataFiles.userIsLoggedIn(dropDown.getValue()) ? "Logout" : "Login");
            try { dataFiles.store(); }catch(Exception _e) { errorPopup(_e); }
        });

        dialog.setTitle("Add User");
        dialog.setHeaderText("Please provide your first name and last initial");
        dialog.setGraphic(new ImageView(new Image(String.valueOf(TimeTracker.class.getResource("images/TalonLogo.png")), 75, 75, true, true)));
        dialog.setContentText("Please enter your name:");

        createUser.setOnAction(e -> {
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()) {
                String name = result.get();
                if(!dataFiles.userExists(name)) {
                    dataFiles.createUser(name);
                    dropDown.getItems().add(name);
                    dropDown.getSelectionModel().select(dropDown.getItems().size() - 1);
                    dialog.getEditor().setText("");
                }
            }
        });

        anchorPane.requestFocus();
    }

    private void errorPopup(Exception e) {
        new Alert(Alert.AlertType.ERROR, "An error occurred:\n"+e.toString(), ButtonType.OK).show();
    }
}