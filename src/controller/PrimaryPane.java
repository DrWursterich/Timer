package controller;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class PrimaryPane {
	@FXML private GridPane root;
	@FXML private Label hoursLabel;
	@FXML private Label minutesLabel;
	@FXML private Label secondsLabel;
	private Time seconds;
	private Thread clockThread = new Thread(() -> {
		Clock clock = Clock.tickSeconds(ZoneId.systemDefault());
		Instant last = clock.instant();
		while(true) {
			Instant current = clock.instant();
			int seconds = (int)(current.getEpochSecond() - last.getEpochSecond());
			if (seconds != 0) {
				if (seconds > 1) {
					this.seconds.add(seconds);
					System.out.println("clock display skipped " + seconds + " seconds");
				} else {
					this.seconds.increment();
				}
				last = current;
			}
		}
	});

	public class Time {
		private int value;
		private int maxValue;
		private Time nextInstance;
		private Label label;
		private Runnable labelRunnable = () ->
				Time.this.label.setText(
						String.format("%02d", Time.this.value));

		public Time(int maxValue, Label label) {
			this(maxValue, label, null);
		}

		public Time(int maxValue, Label label, Time nextInstance) {
			if (maxValue <= 0) {
				throw new IllegalArgumentException(
						"MaxValue cannot be negative or zero");
			}
			if (maxValue > 99) {
				throw new IllegalArgumentException(
						"MaxValue cannot be more than two digits");
			}
			this.maxValue = maxValue;
			this.nextInstance = nextInstance;
			this.label = label;
			this.setValue(0);
		}

		public void setValue(int value) {
			if (value < 0) {
				throw new IllegalArgumentException(
						"Value cannot be negative");
			}
			if (value >= this.maxValue) {
				throw new IllegalArgumentException(
						"Value cannot be higher than its MaxValue");
			}
			this.value = value;
			this.updateLabel();
		}

		public int getValue() {
			return this.value;
		}

		public void add(int amount) {
			int newAmount = this.value + amount;
			if (newAmount >= this.maxValue) {
				if (this.nextInstance != null) {
					this.nextInstance.add(newAmount / this.maxValue);
				}
				newAmount = newAmount % this.maxValue;
			}
			this.setValue(newAmount);
		}

		public void increment() {
			this.value = this.value + 1 == this.maxValue ? 0 : this.value +1;
			if (this.value == 0 && this.nextInstance != null) {
				this.nextInstance.increment();
			}
			this.updateLabel();
		}

		public void decrement() {
			this.value = this.value == 0 ? this.maxValue : this.value -1;
			if (this.value == this.maxValue && this.nextInstance != null) {
				this.nextInstance.decrement();
			}
			this.updateLabel();
		}

		private void updateLabel() {
			if (this.label != null) {
				Platform.runLater(this.labelRunnable);
			}
		}
	}

	public PrimaryPane() {
		this.clockThread.setPriority(Thread.MIN_PRIORITY);
	}

	@FXML
	public void initialize() {
		this.seconds = new Time(
				60, this.secondsLabel,
				new Time(
						60, this.minutesLabel,
						new Time(99, this.hoursLabel)));
		if (!this.clockThread.isAlive()) {
			this.clockThread.start();
		}
	}

	@Override
	public void finalize() {
		if (this.clockThread.isAlive()) {
			this.clockThread.interrupt();
		}
	}

}
