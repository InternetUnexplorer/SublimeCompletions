import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by Alex on 6/18/2016.
 */
public class Controller {
	private Stage stage;
	@FXML private BorderPane parent;

	public Controller(Stage stage) {
		this.stage = stage;
	}

	@FXML public void initialize() {
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
}
