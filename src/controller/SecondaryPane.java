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
	private Button BackwardsButton;
	@FXML
	private Button PauseButton;
	@FXML
	private Button ForwardsButton;
	@FXML
	private Button SettingsButton;
	@FXML
	private Button MoveButton;
	@FXML
	private Button ExitButton;
	@FXML
	private Button StartNewButton;
	@FXML
	private Button RestartButton;
	@FXML
	private Button ModeButton;
	@FXML
	private Button UnpauseButton;

	public SecondaryPane() {}

	@FXML
	public void backwards() {
		Main.getInstance().getTimer().backwards();
	}

	@FXML
	public void pause() {
		Main.getInstance().getTimer().pause();
		this.PauseButton.setVisible(false);
		this.UnpauseButton.setVisible(true);
	}

	@FXML
	public void unpause() {
		Main.getInstance().getTimer().unpause();
		this.PauseButton.setVisible(true);
		this.UnpauseButton.setVisible(false);
	}

	@FXML
	public void forwards() {
		Main.getInstance().getTimer().forwards();
	}

	@FXML
	public void settings() {
	}

	@FXML
	public void move() {
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
	}
}
