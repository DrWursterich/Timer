package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import application.Main;
import control.NumberField;
import timer.Mode;
import timer.RunConfig;
import timer.Time;

public class StartDialog extends Dialog<RunConfig> {
	@FXML
	private VBox root;
	@FXML
	private ListView<RunConfig> prevRunConfigs;
	@FXML
	private ComboBox<Mode> modeSelectBox;
	@FXML
	private NumberField hourField;
	@FXML
	private NumberField minuteField;
	@FXML
	private NumberField secondField;
	private final Time hours = new Time();
	private final Time minutes = new Time(60, this.hours);
	private final Time seconds = new Time(60, this.minutes);

	public StartDialog() {
		super();
		Main.getInstance().setStartDialog(this);
		this.initStyle(StageStyle.UTILITY);
	}

	@FXML
	public void initialize() {
		final DialogPane pane = this.getDialogPane();
		this.hourField.valueProperty().addListener((v, o, n) -> this.hours.setValue((int)n));
		this.minuteField.valueProperty().addListener((v, o, n) -> this.minutes.setValue((int)n));
		this.secondField.valueProperty().addListener((v, o, n) -> this.seconds.setValue((int)n));
		final ButtonType runButton = new ButtonType("Run", ButtonData.APPLY);
		this.prevRunConfigs.setCellFactory(e -> {
			return new ListCell<RunConfig>() {
				@Override
				public void updateItem(RunConfig config, boolean empty) {
					super.updateItem(config, empty);
					if (!empty && config != null) {
						this.setText(config.toString());
						this.installContextMenu();
						this.setOnMouseClicked(e -> {
							if (e.getClickCount() > 1) {
								((Button)pane.lookupButton(runButton)).fire();
							}
						});
					}
				}

				private void installContextMenu() {
					final ContextMenu contextMenu = new ContextMenu();
					final MenuItem runMenuItem = new MenuItem("Run");
					runMenuItem.setOnAction(e -> {
						((Button)pane.lookupButton(runButton)).fire();
					});
					runMenuItem.setAccelerator(KeyCombination.keyCombination("Enter"));
					final MenuItem deleteMenuItem = new MenuItem("Delete");
					deleteMenuItem.setOnAction(e -> {
						final ListView<RunConfig> configList = StartDialog.this.prevRunConfigs;
						Main.getInstance().getPrevRunConfigs().remove(
								configList.getSelectionModel().getSelectedItem());
						configList.setItems(Main.getInstance().getPrevRunConfigs());
						configList.refresh();
					});
					deleteMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));
					contextMenu.getItems().addAll(runMenuItem, deleteMenuItem);
					this.setContextMenu(contextMenu);
				}
			};
		});
		this.prevRunConfigs.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
			if (n != null) {
				this.modeSelectBox.getSelectionModel().select(n.getMode());
				this.seconds.setEntireValue(n.getSeconds());
			}
		});
		this.modeSelectBox.setItems(FXCollections.observableArrayList(Mode.values()));
		pane.setContent(this.root);
		pane.getButtonTypes().addAll(runButton, ButtonType.CANCEL);
		((Button)pane.lookupButton(ButtonType.CANCEL)).setCancelButton(true);
		this.root.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				((Button)pane.lookupButton(runButton)).fire();
			}
		});
		this.prevRunConfigs.setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case ENTER:
				((Button)pane.lookupButton(runButton)).fire();
			case ESCAPE:
				this.hide();
				break;
			case DELETE:
				Main.getInstance().getPrevRunConfigs().remove(
						this.prevRunConfigs.getSelectionModel().getSelectedItem());
				this.prevRunConfigs.setItems(Main.getInstance().getPrevRunConfigs());
				this.prevRunConfigs.refresh();
			default:
				break;
			}
		});
		pane.lookupButton(runButton).disableProperty().bind(
				this.modeSelectBox.getSelectionModel().selectedItemProperty().isNull());
		this.setResultConverter(e -> {
			return runButton.equals(e)
					? new RunConfig(
						StartDialog.this.modeSelectBox.getSelectionModel().getSelectedItem(),
						StartDialog.this.seconds.getEntireValue())
					: null;
		});
		this.setOnShowing(e -> {
			this.prevRunConfigs.setItems(Main.getInstance().getPrevRunConfigs());
			this.prevRunConfigs.refresh();
			this.prevRunConfigs.getSelectionModel().clearSelection();
			this.modeSelectBox.requestFocus();
		});
	}
}
