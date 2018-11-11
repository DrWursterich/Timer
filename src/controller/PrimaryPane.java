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
	@FXML
	private Label leftSeparationLabel;
	@FXML
	private Label rightSeparationLabel;

	public PrimaryPane() {}

	@FXML
	public void initialize() {
		Main.getInstance().getTimer().setPrimaryPane(this);
	}

	public Label getHourLabel() {
		return this.hoursLabel;
	}

	public Label getMinuteLabel() {
		return this.minutesLabel;
	}

	public Label getSecondLabel() {
		return this.secondsLabel;
	}

	public void setSeparationVisible(final boolean value) {
		if (this.leftSeparationLabel != null) {
			this.leftSeparationLabel.setVisible(value);
		}
		if (this.rightSeparationLabel != null) {
			this.rightSeparationLabel.setVisible(value);
		}
	}
}
