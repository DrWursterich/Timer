package controller;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class PrimaryPane {
	@FXML
	private GridPane root;
	@FXML
	private Label hoursLabel;
	@FXML
	private Label minutesLabel;
	@FXML
	private Label secondsLabel;

	public PrimaryPane() {}

	@FXML
	public void initialize() {
		Main.getInstance().getTimer().setTimerLabels(
				this.hoursLabel,
				this.minutesLabel,
				this.secondsLabel);
	}
}
