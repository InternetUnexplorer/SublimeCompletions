/**
 * Created by Alex on 6/18/2016.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
		Controller controller = new Controller(primaryStage);
		loader.setController(controller);
		BorderPane parent = loader.load();
		//parent.getStylesheets().add(getClass().getResource("/dark.css").toExternalForm());
		primaryStage.setScene(
				new Scene(parent, 600, 400)
		);
		primaryStage.setTitle("ST3 Completions Generator");
		primaryStage.show();
	}
}
