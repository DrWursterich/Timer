package timer;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;

public class Timer extends Thread {
	final Clock clock = Clock.tickSeconds(ZoneId.systemDefault());
	final Time hours = new Time();
	final Time minutes = new Time(60, this.hours);
	final Time seconds = new Time(60, this.minutes);
	Instant last = this.clock.instant();
	private volatile boolean terminate = false;
	private final ObjectProperty<Mode> mode = new SimpleObjectProperty<>();
	protected boolean paused = false;

	public void setMode(final Mode mode) {
		final Mode current = this.mode.get();
		if (current != null) {
			current.setTimer(null);
		}
		this.mode.set(mode);
		this.last = this.clock.instant();
		mode.setTimer(this);
		mode.initialize();
		if (this.paused) {
			this.pause();
		}
	}

	public Mode getMode() {
		return this.mode.get();
	}

	public ObjectProperty<Mode> modeProperty() {
		return this.mode;
	}

	@Override
	public void run() {
		while(!this.terminate) {
			this.mode.get().run();
		}
	}

	public void terminate() {
		this.terminate = true;
	}

	public void setTimerLabels(
			final Label hours, final Label minutes, final Label seconds) {
		this.hours.setLabel(hours);
		this.minutes.setLabel(minutes);
		this.seconds.setLabel(seconds);
	}

	public void backwards() {
		this.mode.get().backwards();
	}

	public void pause() {
		this.paused = true;
		this.mode.get().pause();
	}

	public void unpause() {
		this.paused = false;
		this.mode.get().unpause();
	}

	public void forwards() {
		this.mode.get().forwards();
	}

	public void restart() {
		this.mode.get().restart();
	}
}
