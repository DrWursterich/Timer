package timer;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import javafx.scene.control.Label;

public class Timer extends Thread {
	final Clock clock = Clock.tickSeconds(ZoneId.systemDefault());
	final Time hours = new Time();
	final Time minutes = new Time(60, this.hours);
	final Time seconds = new Time(60, this.minutes);
	Instant last = this.clock.instant();
	private volatile boolean terminate = false;
	private Mode mode;

	public void setMode(Mode mode) {
		if (this.mode != null) {
			this.mode.setTimer(null);
		}
		this.mode = mode;
		this.mode.setTimer(this);
		this.mode.initialize();
	}

	@Override
	public void run() {
		while(!this.terminate) {
			this.mode.run();
		}
	}

	public void terminate() {
		this.terminate = true;
	}

	public void setTimerLabels(Label hours, Label minutes, Label seconds) {
		this.hours.setLabel(hours);
		this.minutes.setLabel(minutes);
		this.seconds.setLabel(seconds);
	}

	public void backwards() {
		this.mode.backwards();
	}

	public void pause() {
		this.mode.pause();
	}

	public void forwards() {
		this.mode.forwards();
	}

	public void restart() {
		this.mode.restart();
	}
}
