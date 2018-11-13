package timer;

import javafx.util.Pair;

public class RunConfig extends Pair<Mode, Integer>{
	private static final long serialVersionUID = 2868109897538766631L;

	public RunConfig(Mode mode, int seconds) {
		super(mode, seconds);
	}

	public Mode getMode() {
		return this.getKey();
	}

	public int getSeconds() {
		return this.getValue();
	}

	@Override
	public String toString() {
		return this.getTimeLabel(this.getValue())
				+ " ("
				+ this.getKey().toString()
				+ ")";
	}

	private String getTimeLabel(int seconds) {
		String ret = String.format("%02d:", Math.floorDiv(seconds, 60 * 60));
		seconds %= 60 * 60;
		ret += String.format("%02d:", Math.floorDiv(seconds, 60));
		return ret + String.format("%02d", seconds % 60);
	}
}
