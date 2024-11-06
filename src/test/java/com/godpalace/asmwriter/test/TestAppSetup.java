package com.godpalace.asmwriter.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TestAppSetup extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Hello World");

        Button button = new Button("Are you sure?");
        button.setOnAction(e -> button.setText("sure"));

        BorderPane root = new BorderPane(button);
        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);

        stage.show();
    }
}
