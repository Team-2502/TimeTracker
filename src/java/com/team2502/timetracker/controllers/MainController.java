package com.team2502.timetracker.controllers;

import com.team2502.timetracker.TimeTracker;
import com.team2502.timetracker.internal.JsonData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label mainLabel;

    @FXML
    private ComboBox<String> dropDown;

    @FXML
    private Button login;

    @FXML
    private Label hoursLabel;

    @FXML
    private Button createUser;

    @FXML
    private ImageView image;

    @FXML
    private LeaderBoardController leaderBoardController;

    @FXML
    private TabPane parentTabPane;

    private JsonData dataFiles;
    private TextInputDialog dialog = new TextInputDialog();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO: Find a good way to resize when also moving height
//        image.fitHeightProperty().bind(anchorPane.heightProperty().multiply(4));
        anchorPane.widthProperty().addListener(((observable, oldValue, newValue) -> {
            mainLabel.setLayoutX((newValue.doubleValue() / 2) - mainLabel.getPrefWidth() / 2);
            image.setLayoutX((newValue.doubleValue() / 2) - (image.getFitWidth() / 2));
            dropDown.setLayoutX((newValue.doubleValue() / 2) - 100);
            login.setLayoutX((newValue.doubleValue() / 2) + 50);
            hoursLabel.setLayoutX((newValue.doubleValue() / 2) - (hoursLabel.getPrefWidth() / 2));
            createUser.setLayoutX(newValue.intValue() - 87);
        }));

        anchorPane.heightProperty().addListener(((observable, oldValue, newValue) -> {
            createUser.setLayoutY(newValue.intValue() - 41);
        }));

        try {
            dataFiles = new JsonData("data.json");
        } catch (Exception e) {
            errorPopup(e);
        }

        dataFiles.recalculateTotalTimes();

//        home.setOnAction(e -> System.out.println("Home"));
//        leaderboard.setOnAction(e -> System.out.println("Leaderboard"));
//        settings.setOnAction(e -> System.out.println("Settings"));

//        leaderboard.setOnAction(e -> new Alert(Alert.AlertType.ERROR, "Leaderboards not here yet. soon tm", ButtonType.OK).show());

        dropDown.getItems().addAll(Arrays.asList(dataFiles.getUsers()));
        dropDown.setOnAction(e -> {
            login.setText(dataFiles.userIsLoggedIn(dropDown.getValue()) ? "Logout" : "Login");
            hoursLabel.setText("Hours: " + dataFiles.getUserTotalTime(dropDown.getValue()) / 60);
        });

        if (!dropDown.getItems().isEmpty())
            dropDown.getSelectionModel().select(0);

        hoursLabel.setText("Hours: " + dataFiles.getUserTotalTime(dropDown.getValue()) / 60);
        login.setText(dataFiles.userIsLoggedIn(dropDown.getValue()) ? "Logout" : "Login");

        login.setOnAction(e -> {
            dataFiles.toggleUserLogin(dropDown.getValue());
            login.setText(dataFiles.userIsLoggedIn(dropDown.getValue()) ? "Logout" : "Login");
            hoursLabel.setText("Hours: " + dataFiles.getUserTotalTime(dropDown.getValue()) / 60);
            try {
                dataFiles.store();
            } catch (Exception _e) {
                errorPopup(_e);
            }
        });

        dialog.setTitle("Add User");
        dialog.setHeaderText("Please provide your first name and last initial");
        dialog.setGraphic(new ImageView(new Image(String.valueOf(TimeTracker.class.getResource("images/TalonLogo.png")), 75, 75, true, true)));
        dialog.setContentText("Please enter your name:");

        createUser.setOnAction(e -> {
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String name = result.get();
                if (name.equals(""))
                    return;

                if (dataFiles.userExists(name)) {
                    dataFiles.createUser(name);
                    dropDown.getItems().add(name);
                    dropDown.getSelectionModel().select(dropDown.getItems().size() - 1);
                    dialog.getEditor().setText("");
                } else {
                    dropDown.getSelectionModel().select(name);
                }
            }
        });

        anchorPane.requestFocus();
    }

    public void toLeaderboard() throws IOException {

        leaderBoardController.setJsonData(dataFiles);
        leaderBoardController.init();

    }

    private void setupMenu(Menu menu) {
        menu.getItems().add(new MenuItem("dummy"));
        menu.showingProperty().addListener((a, b, c) -> {
            if (c) menu.getItems().get(0).fire();
        });
    }

    private void errorPopup(Exception e) {
        new Alert(Alert.AlertType.ERROR, "An error occurred:\n" + e.toString(), ButtonType.OK).showAndWait();
    }
}