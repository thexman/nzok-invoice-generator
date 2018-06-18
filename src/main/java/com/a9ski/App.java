package com.a9ski;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
		final Parent root = (Parent)loader.load();
		MainWindowController controller = (MainWindowController)loader.getController();
		controller.setStage(stage);
		controller.setApplication(this);

		Scene scene = new Scene(root, 640, 200);

		stage.setTitle("XML file extractor");
		stage.setScene(scene);		
		//getHostServices()
		stage.show();
	}

}
