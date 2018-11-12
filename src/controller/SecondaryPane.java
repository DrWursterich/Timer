package controller;

import application.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class SecondaryPane {
	@FXML
	private GridPane root;
	@FXML
	private Button backwardsButton;
	@FXML
	private Button pauseButton;
	@FXML
	private Button forwardsButton;
	@FXML
	private Button settingsButton;
	@FXML
	private Button moveButton;
	@FXML
	private Button exitButton;
	@FXML
	private Button startNewButton;
	@FXML
	private Button restartButton;
	@FXML
	private Button modeButton;
	@FXML
	private Button unpauseButton;

	public SecondaryPane() {}

	@FXML
	public void initialize() {
		if (this.moveButton != null) {
			this.moveButton.setOnMousePressed(Main.getInstance()::startDrag);
			this.moveButton.setOnMouseDragged(Main.getInstance()::dragging);
			this.moveButton.setOnMouseReleased(Main.getInstance()::endDrag);
		}
	}

	@FXML
	public void backwards() {
		Main.getInstance().getTimer().backwards();
	}

	@FXML
	public void pause() {
		Main.getInstance().getTimer().pause();
		this.pauseButton.setVisible(false);
		this.unpauseButton.setVisible(true);
	}

	@FXML
	public void unpause() {
		Main.getInstance().getTimer().unpause();
		this.pauseButton.setVisible(true);
		this.unpauseButton.setVisible(false);
	}

	@FXML
	public void forwards() {
		Main.getInstance().getTimer().forwards();
	}

	@FXML
	public void settings() {
	}

	@FXML
	public void exit() {
		Platform.exit();
	}

	@FXML
	public void startNew() {
		this.restart();
	}

	@FXML
	public void restart() {
		Main.getInstance().getTimer().restart();
	}

	@FXML
	public void chooseMode() {
		Main.getInstance().chooseMode();
	}
}
