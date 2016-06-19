import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.effect.GaussianBlur;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by Alex on 6/19/2016.
 */
public class InfoController {
	@FXML private TextArea text;

	@FXML
	public void initialize() {
		text.setText(new Scanner(getClass().getResourceAsStream("/info-help"), "UTF-8").useDelimiter("\\A").next());
	}

	@FXML private void viewOnGithub() throws URISyntaxException, IOException {
		java.awt.Desktop.getDesktop().browse(new URI("https://github.com/InternetUnexplorer/SublimeCompletions"));
	}
}
