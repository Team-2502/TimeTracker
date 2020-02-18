package com.team2502.timetracker.controllers;

import com.team2502.timetracker.TimeTracker;
import com.team2502.timetracker.internal.JsonData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

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

    @FXML
    private ScrollPane scrollPane;

    private JsonData dataFiles;
    private TextInputDialog dialog = new TextInputDialog();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        image.fitHeightProperty().bind(anchorPane.heightProperty().multiply(4));
        anchorPane.widthProperty().addListener(((observable, oldValue, newValue) -> {
            mainLabel.setLayoutX((newValue.doubleValue() / 2) - mainLabel.getPrefWidth() / 2);
            image.setLayoutX((newValue.doubleValue() / 2) - (image.getFitWidth() / 2));
            dropDown.setLayoutX((newValue.doubleValue() / 2) - 100);
            login.setLayoutX((newValue.doubleValue() / 2) + 50);
            hoursLabel.setLayoutX((newValue.doubleValue() / 2) - (hoursLabel.getPrefWidth() / 2));
            createUser.setLayoutX(newValue.intValue() - 87);
        }));

        try {
            dataFiles = new JsonData("data.json");
        } catch (Exception e) {
            errorPopup(e);
        }

        dataFiles.recalculateTotalTimes();

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
            setupScrollPane();

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

        setupScrollPane();
        anchorPane.requestFocus();
    }

    private void setupScrollPane() {
        VBox vBox = new VBox();
        String[] users = dataFiles.getLoggedInUsers();
        for(int i = 0; i < users.length; i++) {
            AnchorPane anchorPane = new AnchorPane();
            Label number = new Label(String.valueOf(i+1));
            Label name = new Label(users[i]);
            Button button = new Button("Logout");
            Separator separator =  new Separator();
            number.setFont(Font.font("Impact", 16));
            name.setFont(Font.font("Impact", 16));

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

            button.setAlignment(Pos.CENTER);
            button.setPrefWidth(100);
            button.setPrefHeight(25);
            button.setLayoutX(420);
            button.setLayoutY(10);

            separator.setPrefWidth(scrollPane.getPrefWidth());
            separator.setPrefHeight(5);
            anchorPane.getChildren().add(number);
            anchorPane.getChildren().add(name);
            anchorPane.getChildren().add(button);
            anchorPane.getChildren().add(separator);
            vBox.getChildren().add(anchorPane);
            vBox.setSpacing(10);

            scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> {
                button.setLayoutX(newValue.intValue() - 130);
                separator.setPrefWidth(scrollPane.getWidth());
            });

            String user = users[i];
            button.setOnAction(e -> {
                dataFiles.toggleUserLogin(user);
                setupScrollPane();

                try {
                    dataFiles.store();
                } catch (Exception _e) {
                    errorPopup(_e);
                }
            });
        }

        scrollPane.setContent(vBox);
    }

    public void toLeaderboard() throws IOException {

        leaderBoardController.setJsonData(dataFiles);
        leaderBoardController.init();

    }

    private void errorPopup(Exception e) {
        new Alert(Alert.AlertType.ERROR, "An error occurred:\n" + e.toString(), ButtonType.OK).showAndWait();
    }
}