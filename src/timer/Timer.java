package timer;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import controller.PrimaryPane;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Timer extends Thread {
	final Clock clock = Clock.tickSeconds(ZoneId.systemDefault());
	final Time hours = new Time();
	final Time minutes = new Time(60, this.hours);
	final Time seconds = new Time(60, this.minutes);
	Instant last = this.clock.instant();
	private volatile boolean terminate = false;
	private final ObjectProperty<Mode> mode = new SimpleObjectProperty<>();
	protected boolean paused = false;
	private PrimaryPane primaryPane;

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

	public void setPrimaryPane(final PrimaryPane pane) {
		this.primaryPane = pane;
		this.hours.setLabel(pane.getHourLabel());
		this.minutes.setLabel(pane.getMinuteLabel());
		this.seconds.setLabel(pane.getSecondLabel());
	}

	@Override
	public void run() {
		while(!this.terminate) {
			this.mode.get().run();
			if (this.primaryPane != null) {
				this.primaryPane.setSeparationVisible(
						this.clock.instant().getEpochSecond() % 2 == 1
						|| this.paused);
			}
		}
	}

	public void terminate() {
		this.terminate = true;
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
