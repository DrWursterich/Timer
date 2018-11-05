package controller;

import java.io.File;
import java.util.Arrays;

import application.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import timer.Mode;

public class ModeSelect extends Dialog<Mode> {
	@FXML
	private VBox root;
	@FXML
	private ListView<Mode> modeList;
	private final static Image SELECTED = new Image(
			new File("images/SelectedIcon.png").toURI().toString());

	public ModeSelect() {
		super();
		Main.getInstance().setModeSelectDialog(this);
		this.initStyle(StageStyle.UTILITY);
	}

	@FXML
	public void initialize() {
		this.modeList.setCellFactory(e -> {
			return new ListCell<Mode>() {
				@Override
				public void updateItem(Mode mode, boolean empty) {
					super.updateItem(mode, empty);
					if (!empty && mode != null) {
						this.setText(mode.toString());
						this.setGraphicTextGap(5);
						this.setGraphic(mode.equals(Main.getInstance().getTimer().getMode())
								? new ImageView(ModeSelect.SELECTED)
								: null);
					}
				}
			};
		});
		this.getDialogPane().setContent(this.root);
		this.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
		this.getDialogPane().lookupButton(ButtonType.APPLY).disableProperty().bind(
				this.modeList.getSelectionModel().selectedItemProperty().isNull().or(
					this.modeList.getSelectionModel().selectedItemProperty().isEqualTo(
						Main.getInstance().getTimer().modeProperty())));
		this.setResultConverter(e -> {
			return ButtonType.APPLY.equals(e)
					? ModeSelect.this.modeList.getSelectionModel().getSelectedItem()
					: null;
		});
		this.setOnShowing(e -> {
			this.modeList.setItems(FXCollections.observableList(
					Arrays.asList(Mode.values())));
			this.modeList.refresh();
		});
	}
}
