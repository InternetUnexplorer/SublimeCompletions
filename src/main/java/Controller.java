import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Created by Alex on 6/18/2016.
 */
public class Controller {
	private Stage stage;
	@FXML private BorderPane parent;
	@FXML private ProgressBar progress;
	@FXML private Button mainButton;
	@FXML private TextArea log;

	@FXML private TextField iDirField;
	@FXML private TextField oDirField;

	@FXML private BorderPane iDirP;
	@FXML private BorderPane oDirP;

	private Properties properties;
	private final String propFile = System.getProperty("java.io.tmpdir") + File.separator + "ST3-Completions-Generator.properties";

	public Controller(Stage stage) {
		this.stage = stage;
	}

	@FXML public void initialize() {
		initializeUI();
		log.setText("Ready");
		properties = new Properties();
		try {
			properties.load(new FileInputStream(propFile));
		} catch (Exception e) {}
		iDirField.setText(properties.getProperty("input-directory-location", System.getProperty("user.home") + File.separator + "Documents"));
		oDirField.setText(properties.getProperty("output-directory-location", System.getProperty("user.home") + File.separator + "Documents"));
		mainButton.setOnAction((event) -> {
			initializeRun();
		});
		System.out.println(System.getProperties().toString());
		//Fade in effect
		GaussianBlur effect = new GaussianBlur(20);
		parent.setEffect(effect);
		Timeline timeline = new Timeline();
			timeline.getKeyFrames().addAll(
					new KeyFrame(Duration.ZERO,
							new KeyValue(effect.radiusProperty(), 20)
					),
					new KeyFrame(Duration.seconds(1),
							new KeyValue(effect.radiusProperty(), 0)
					)
			);
		// play 40s of animation
		timeline.play();
	}

	@FXML public void setInputDir() {
		DirectoryChooser dirC = new DirectoryChooser();
		File f = new File(iDirField.getText());
		if(f.isDirectory()) {
			dirC.setInitialDirectory(f);
		} else {
			dirC.setInitialDirectory(new File(System.getProperty("user.home")));
		}
		dirC.setTitle("Choose input directory");
		File res = dirC.showDialog(stage);
		if(res != null) {
			iDirField.setText(res.getPath());
			properties.setProperty("input-directory-location", res.getPath());
			storeProps();
		}
	}

	@FXML public void setOutputDir() {
		DirectoryChooser dirC = new DirectoryChooser();
		File f = new File(oDirField.getText());
		if(f.isDirectory()) {
			dirC.setInitialDirectory(f);
		} else {
			dirC.setInitialDirectory(new File(System.getProperty("user.home")));
		}
		dirC.setTitle("Choose output directory");
		File res = dirC.showDialog(stage);
		if(res != null) {
			oDirField.setText(res.getPath());
			properties.setProperty("output-directory-location", res.getPath());
			storeProps();
		}
	}

	private void initializeUI() {
		setNodeVisible(progress, false);
		setNodeVisible(mainButton, true);
		setNodeVisible(iDirP, true);
		setNodeVisible(oDirP, true);
	}

	private void initializeRun() {
		setNodeVisible(progress, true);
		setNodeVisible(mainButton, false);
		setNodeVisible(iDirP, false);
		setNodeVisible(oDirP, false);
		log.setText("");
		progress.setProgress(-1);
		new Thread(() -> {
			new Generator(
					new File(iDirField.getText()),
					new File(oDirField.getText()),
					log,
					progress
			).run();
			Platform.runLater(
					this::initializeUI
			);
		}).start();
	}

	private void setNodeVisible(Node node, boolean visible) {
		if(visible) {
			node.setManaged(true);
			node.setVisible(true);
		} else {
			node.setManaged(false);
			node.setVisible(false);
		}
	}

	private void storeProps() {
		try {
			properties.store(new FileOutputStream(propFile), "Preferences for ST3-Completions-Generator");
		} catch (IOException e) {}
	}
}
