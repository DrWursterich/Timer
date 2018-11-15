package application;

import java.util.Optional;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import ressource.RessourceManager;
import timer.Mode;
import timer.RunConfig;
import timer.Timer;

public class Main extends Application {
	private double screenWidth;
	private double screenHeight;
	private Parent primaryPane;
	private Parent secondaryPane;
	private Stage primaryStage;
	private Pane root;
	private Scene scene;
	private ParallelTransition enterTransition;
	private ParallelTransition exitTransition;
	private boolean animationIsPlaying = false;
	private boolean mouseIsHovering = false;
	private static Main instance;
	private final Timer timer = new Timer();
	private Dialog<RunConfig> startDialog = new Dialog<>();
	private Dialog<Mode> modeSelect = new Dialog<>();
	private final ObservableList<RunConfig> prevRunConfigs = FXCollections.observableArrayList();
	private double xOffset = 0;
	private double yOffset = 0;

	@Override
	public void start(final Stage primaryStage) {
		try {
			Main.instance = this;
			this.timer.setPriority(Thread.MIN_PRIORITY);
			this.timer.setMode(Mode.TIMER);
			this.timer.start();
			this.primaryStage = primaryStage;
			this.primaryPane = RessourceManager.getRessource(
					"application.PrimaryPane");
			this.secondaryPane = RessourceManager.getRessource(
					"application.SecondaryPane");
			this.secondaryPane.setOpacity(0);
			RessourceManager.getRessource("application.ModeSelect");
			RessourceManager.getRessource("application.StartDialog");
			this.root = new StackPane(this.secondaryPane, this.primaryPane);
			this.root.setBackground(null);
			this.scene = new Scene(this.root, 360, 80);
			this.scene.setFill(Color.color(0, 0, 0, 0.01));
			final Rectangle2D screen = Screen.getPrimary().getVisualBounds();
			this.screenWidth = screen.getMaxX() - this.scene.getWidth();
			this.screenHeight = screen.getMaxY() - this.scene.getHeight();
			this.initAnimations();
			this.primaryStage.fullScreenProperty().addListener(e -> {
				if (this.primaryStage.isFullScreen()) {
					this.primaryStage.setFullScreen(false);
				}
			});
			this.primaryStage.setScene(this.scene);
			this.primaryStage.setX(this.screenWidth);
			this.primaryStage.setY(this.screenHeight);
			this.primaryStage.setAlwaysOnTop(true);
			this.primaryStage.initStyle(StageStyle.TRANSPARENT);
			this.primaryStage.show();
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

	public ObservableList<RunConfig> getPrevRunConfigs() {
		return this.prevRunConfigs;
	}

	public void setModeSelectDialog(Dialog<Mode> dialog) {
		this.modeSelect = dialog;
	}

	public void setStartDialog(Dialog<RunConfig> dialog) {
		this.startDialog = dialog;
	}

	public void chooseMode() {
		Optional<Mode> result = this.modeSelect.showAndWait();
		if (result != null && result.isPresent()) {
			this.timer.setMode(result.get());
		}
	}

	public void startNew() {
		Optional<RunConfig> result = this.startDialog.showAndWait();
		if (result != null && result.isPresent()) {
			this.runConfig(result.get());
		}
	}

	public void runConfig(RunConfig runConfig) {
		if (this.prevRunConfigs.contains(runConfig)) {
			this.prevRunConfigs.removeAll(runConfig);
		}
		this.prevRunConfigs.add(0, runConfig);
		this.timer.setMode(runConfig.getMode());
		this.timer.startAt(runConfig.getSeconds());
	}

	public void startDrag(final MouseEvent mouseEvent) {
		this.xOffset = this.primaryStage.getX() - mouseEvent.getScreenX();
		this.yOffset = this.primaryStage.getY() - mouseEvent.getScreenY();
	}

	public void dragging(final MouseEvent mouseEvent) {
		final double newX = mouseEvent.getScreenX() + this.xOffset;
		final double newY = mouseEvent.getScreenY() + this.yOffset;
		this.primaryStage.setX(this.clamp(newX, 0, this.screenWidth));
		this.primaryStage.setY(this.clamp(newY, 0, this.screenHeight));
	}

	public void endDrag(final MouseEvent mouseEvent) {
		this.xOffset = 0;
		this.yOffset = 0;
		if (!this.mouseIsHovering && !this.animationIsPlaying) {
			this.exitTransition.play();
		}
	}

	private double clamp(
			final double value,
			final double min,
			final double max) {
		return Math.max(Math.min(value, max), min);
	}

	private void initAnimations() {
		final Duration duration = Duration.seconds(0.25);
		final double centerX = this.scene.getWidth() / 2;
		final double centerY = this.scene.getHeight() / 2;
		final double cornerX = this.scene.getWidth() * 0.75;
		final double cornerY = this.scene.getHeight() * 0.75;

		final ScaleTransition zoomIn = new ScaleTransition(
				duration,
				this.primaryPane);
		final PathTransition toCorner = new PathTransition(
				duration,
				new Line(centerX, centerY, cornerX, cornerY),
				this.primaryPane);
		final FadeTransition blendIn = new FadeTransition(
				duration,
				this.secondaryPane);
		this.enterTransition = new ParallelTransition(
				zoomIn,
				toCorner,
				blendIn);

		final ScaleTransition zoomOut = new ScaleTransition(
				duration, this.primaryPane);
		final PathTransition toCenter = new PathTransition(
				duration,
				new Line(cornerX, cornerY, centerX, centerY),
				this.primaryPane);
		final FadeTransition blendOut = new FadeTransition(
				duration, this.secondaryPane);
		this.exitTransition = new ParallelTransition(
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

		this.enterTransition.setOnFinished(e -> {
			if (!this.mouseIsHovering
					&& this.xOffset == 0 && this.yOffset == 0) {
				this.exitTransition.play();
			} else {
				this.animationIsPlaying = false;
			}
		});
		this.exitTransition.setOnFinished(e -> {
			if (this.mouseIsHovering
					&& this.xOffset == 0 && this.yOffset == 0) {
				this.enterTransition.play();
			} else {
				this.animationIsPlaying = false;
			}
		});

		this.scene.setOnMouseEntered(e -> {
			this.mouseIsHovering = true;
			if (!this.animationIsPlaying
					&& this.xOffset == 0 && this.yOffset == 0) {
				this.enterTransition.play();
			}
		});
		this.scene.setOnMouseExited(e -> {
			this.mouseIsHovering = false;
			if (!this.animationIsPlaying
					&& this.xOffset == 0 && this.yOffset == 0) {
				this.exitTransition.play();
			}
		});
	}

	@Override
	public void stop() {
		this.timer.terminate();
	}
}
