package application;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import ressource.RessourceManager;
import timer.Mode;
import timer.Timer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Main extends Application {
	private final Rectangle2D SCREEN = Screen.getPrimary().getVisualBounds();
	private Parent primaryPane;
	private Parent secondaryPane;
	private Pane root;
	private Scene scene;
	private boolean animationIsPlaying = false;
	private boolean mouseIsHovering = false;
	private static Main instance;
	private final Timer timer = new Timer();

	@Override
	public void start(Stage primaryStage) {
		try {
			Main.instance = this;
			this.timer.setPriority(Thread.MIN_PRIORITY);
			this.timer.setMode(Mode.TIMER);
			this.timer.start();
			this.primaryPane = RessourceManager.getRessource(
					"application.PrimaryPane");
			this.secondaryPane = RessourceManager.getRessource(
					"application.SecondaryPane");
			this.secondaryPane.setOpacity(0);
			this.root = new StackPane(this.secondaryPane, this.primaryPane);
			this.root.setBackground(null);
			this.scene = new Scene(this.root, 360, 80);
			this.scene.setFill(Color.color(0.5, 0.5, 0.5, 0.01));
			this.initAnimations();
			primaryStage.setScene(this.scene);
			primaryStage.setX(this.SCREEN.getMaxX() - this.scene.getWidth());
			primaryStage.setY(this.SCREEN.getMaxY() - this.scene.getHeight());
			primaryStage.setAlwaysOnTop(true);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String...args) {
		Application.launch(args);
	}

	public static Main getInstance() {
		return Main.instance;
	}

	public Timer getTimer() {
		return this.timer;
	}

	private void initAnimations() {
		Duration duration = Duration.seconds(0.25);
		double centerX = this.scene.getWidth() / 2;
		double centerY = this.scene.getHeight() / 2;
		double cornerX = this.scene.getWidth() * 0.75;
		double cornerY = this.scene.getHeight() * 0.75;

		ScaleTransition zoomIn = new ScaleTransition(
				duration,
				this.primaryPane);
		PathTransition toCorner = new PathTransition(
				duration,
				new Line(centerX, centerY, cornerX, cornerY),
				this.primaryPane);
		FadeTransition blendIn = new FadeTransition(
				duration,
				this.secondaryPane);
		ParallelTransition enterTransition = new ParallelTransition(
				zoomIn,
				toCorner,
				blendIn);

		ScaleTransition zoomOut = new ScaleTransition(
				duration, this.primaryPane);
		PathTransition toCenter = new PathTransition(
				duration,
				new Line(cornerX, cornerY, centerX, centerY),
				this.primaryPane);
		FadeTransition blendOut = new FadeTransition(
				duration, this.secondaryPane);
		ParallelTransition exitTransition = new ParallelTransition(
				zoomOut,
				toCenter,
				blendOut);

		zoomIn.setFromX(1);
		zoomIn.setFromY(1);
		zoomIn.setToX(0.5);
		zoomIn.setToY(0.5);
		zoomOut.setFromX(0.5);
		zoomOut.setFromY(0.5);
		zoomOut.setToX(1);
		zoomOut.setToY(1);
		blendIn.setFromValue(0);
		blendIn.setToValue(1);
		blendOut.setFromValue(1);
		blendOut.setToValue(0);

		enterTransition.setOnFinished(e -> {
			if (!this.mouseIsHovering) {
				exitTransition.play();
			} else {
				this.animationIsPlaying = false;
			}
		});
		exitTransition.setOnFinished(e -> {
			if (this.mouseIsHovering) {
				enterTransition.play();
			} else {
				this.animationIsPlaying = false;
			}
		});

		this.scene.setOnMouseEntered(e -> {
			this.mouseIsHovering = true;
			if (!this.animationIsPlaying) {
				enterTransition.play();
			}
		});
		this.scene.setOnMouseExited(e -> {
			this.mouseIsHovering = false;
			if (!this.animationIsPlaying) {
				exitTransition.play();
			}
		});
	}

	@Override
	public void stop() {
		this.timer.terminate();
	}
}
