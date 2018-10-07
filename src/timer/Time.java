package timer;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Time {
	private int value;
	private int maxValue;
	private Time nextInstance;
	private Label label;
	private Runnable labelRunnable = () ->
			Time.this.label.setText(
					String.format("%02d", Time.this.value));

	public Time() {
		this(100, null, null);
	}

	public Time(int maxValue) {
		this(maxValue, null, null);
	}

	public Time(int maxValue, Time nextInstance) {
		this(maxValue, null, nextInstance);
	}

	public Time(int maxValue, Label label) {
		this(maxValue, label, null);
	}

	public Time(int maxValue, Label label, Time nextInstance) {
		if (maxValue <= 0) {
			throw new IllegalArgumentException(
					"MaxValue cannot be negative or zero");
		}
		if (maxValue > 100) {
			throw new IllegalArgumentException(
					"MaxValue cannot be higher than 100");
		}
		this.maxValue = maxValue;
		this.nextInstance = nextInstance;
		this.label = label;
		this.setValue(0);
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public Label getLabel() {
		return this.label;
	}

	public void setValue(int value) {
		if (value < 0) {
			throw new IllegalArgumentException(
					"Value(" + value + ") cannot be negative");
		}
		if (value >= this.maxValue) {
			throw new IllegalArgumentException(
					"Value(" + value + ") "
					+ "cannot be higher than its MaxValue(" + this.maxValue + ")");
		}
		this.value = value;
		this.updateLabel();
	}

	public int getValue() {
		return this.value;
	}

	public void setEntireValue(int value) {
		if (value < 0) {
			value = 0;
		}
		if (this.nextInstance != null) {
			this.nextInstance.setEntireValue(value / this.maxValue);
		}
		this.setValue(value % this.maxValue);
	}

	public int getMaxValue() {
		return this.maxValue;
	}

	public int getEntireValue() {
		return this.value + (this.nextInstance != null
					? this.nextInstance.getEntireValue() * this.maxValue
					: 0);
	}

	public void add(int amount) {
		int newAmount = this.value + amount;
		if (newAmount >= this.maxValue && this.nextInstance != null) {
			this.nextInstance.add(newAmount / this.maxValue);
		}
		this.setValue(newAmount % this.maxValue);
	}

	public void subtract(int amount) {
		int newAmount = this.value - amount;
		if (newAmount < 0) {
			if (this.nextInstance != null) {
				this.nextInstance.subtract(1 - newAmount / this.maxValue);
			}
			newAmount = this.maxValue - (-newAmount % this.maxValue) - 1;
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
		this.value = (this.value == 0 ? this.maxValue : this.value) - 1;
		if (this.value + 1 == this.maxValue && this.nextInstance != null) {
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
