package timer;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;


public enum Mode {
	STOP_WATCH("Stop Watch") {
		public final double SKIP_PERCENTAGE = 0.15;

		@Override
		public void run() {
			Instant current = this.clock.instant();
			int seconds = (int)(current.getEpochSecond() - this.last.getEpochSecond());
			if (seconds != 0) {
				if (seconds > 1) {
					this.seconds.add(seconds);
					System.out.println("clock display skipped " + seconds + " seconds");
				} else {
					this.seconds.increment();
				}
				this.last = current;
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				System.out.println("thread could not be paused");
			}
		}

		@Override
		public void backwards() {
			this.seconds.subtract((int)Math.ceil(
					this.seconds.getEntireValue() * this.SKIP_PERCENTAGE));
		}

		@Override
		public void forwards() {
			this.seconds.add((int)(Math.ceil(
					this.seconds.getEntireValue() * this.SKIP_PERCENTAGE)));
		}

		@Override
		public void startNew() {
		}

		@Override
		public void restart() {
			this.seconds.setEntireValue(0);
		}
	},

	TIMER("Timer") {
		public final double SKIP_PERCENTAGE = 0.15;

		@Override
		public void run() {
			Instant current = this.clock.instant();
			int seconds = (int)(current.getEpochSecond() - this.last.getEpochSecond());
			if (seconds != 0) {
				if (seconds > 1) {
					this.seconds.subtract(seconds);
					System.out.println("clock display skipped " + seconds + " seconds");
				} else {
					this.seconds.decrement();
				}
				this.last = current;
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				System.out.println("thread could not be paused");
			}
		}

		@Override
		public void backwards() {
			this.seconds.add((int)(Math.ceil(
					this.seconds.getEntireValue() * this.SKIP_PERCENTAGE)));
		}

		@Override
		public void forwards() {
			this.seconds.subtract((int)Math.ceil(
					this.seconds.getEntireValue() * this.SKIP_PERCENTAGE));
		}

		@Override
		public void startNew() {
			this.restart();
		}

		@Override
		public void restart() {
			this.seconds.setEntireValue(10 * 60);
		}

		@Override
		public void initialize() {
			this.seconds.setEntireValue(10 * 60);
		}
	};

	protected Timer timer;
	protected Clock clock;
	protected Time hours;
	protected Time minutes;
	protected Time seconds;
	protected Instant last;
	protected String name;

	Mode (String name) {
		this.name = name;
	}

	protected void setTimer(Timer timer) {
		this.timer = timer;
		this.clock = timer != null ? timer.clock : null;
		this.hours = timer != null ? timer.hours : null;
		this.minutes = timer != null ? timer.minutes : null;
		this.seconds = timer != null ? timer.seconds : null;
		this.last = timer != null ? timer.last : null;
	}

	protected void initialize() {}

	public void pause() {
		this.clock = Clock.fixed(this.clock.instant(), ZoneId.systemDefault());
	}

	public void unpause() {
		this.clock = Clock.tickSeconds(ZoneId.systemDefault());
		this.last = this.clock.instant();
	}

	public abstract void run();

	public abstract void backwards();

	public abstract void forwards();

	public abstract void startNew();

	public abstract void restart();

	@Override
	public String toString() {
		return this.name != null ? this.name : super.toString();
	}
}
