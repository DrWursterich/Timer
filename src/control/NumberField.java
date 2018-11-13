package control;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

public class NumberField extends TextField {
	private int minValue;
	private int maxValue;
	private final IntegerProperty value = new SimpleIntegerProperty(0);

	public NumberField() {
		this(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	public NumberField(final int value) {
		this(value, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	public NumberField(final int minValue, final int maxValue) {
		this(0, minValue, maxValue);
	}

	public NumberField(final int value, final int minValue, final int maxValue) {
		super();
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.setTextFormatter(new TextFormatter<>(new StringConverter<String>() {
			@Override
			public String toString(String string) {
				return this.formatString(string);
			}

			@Override
			public String fromString(String string) {
				return this.formatString(string);
			}

			private String formatString(String string) {
				if ("".equals(string)) {
					return "00";
				}
				int ret = minValue;
				try {
					ret = Math.max(NumberField.this.minValue, Integer.parseInt(string));
				} catch (NumberFormatException e) {
					return "00";
				}
				return String.format("%02d", Math.min(ret, NumberField.this.maxValue));
			}
		}));
		this.textProperty().addListener((v, o, n) -> {
			try {
				this.value.setValue(Integer.parseInt(n));
			} catch (NumberFormatException e) {
				this.setText("00");
			}
		});
		this.value.setValue(value);
	}

	public int getValue() {
		try {
			return this.getText() == null ? 0 : Integer.parseInt(this.getText());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public void setValue(final int value) {
		this.setText(Integer.toString(value));
	}

	public int getMinValue() {
		return this.minValue;
	}

	public void setMinValue(final int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return this.maxValue;
	}

	public void setMaxValue(final int maxValue) {
		this.maxValue = maxValue;
	}

	public IntegerProperty valueProperty() {
		return this.value;
	}
}
