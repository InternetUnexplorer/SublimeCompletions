/**
 * Created by Alex on 6/18/2016.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		//Load the help / info
		Stage infoStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Info.fxml"));
		loader.setController(new InfoController());
		BorderPane infoParent = loader.load();
		infoStage.setScene(
				new Scene(infoParent, 350, 400)
		);
		infoStage.setTitle("About / Help");
		infoStage.getIcons().clear();
		infoStage.getIcons().add(new Image(getClass().getResource("/icon.png").toExternalForm()));

		//Load the main UI
		loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
		Controller controller = new Controller(primaryStage, infoStage);
		loader.setController(controller);
		BorderPane parent = loader.load();
		//parent.getStylesheets().add(getClass().getResource("/dark.css").toExternalForm());
		primaryStage.setScene(
				new Scene(parent, 600, 400)
		);
		primaryStage.setTitle("ST3 Completions Generator");
		primaryStage.getIcons().clear();
		primaryStage.getIcons().add(new Image(getClass().getResource("/icon.png").toExternalForm()));

		primaryStage.show();
	}
}
