package com.elo7.nightfall.riposte;

import com.elo7.nightfall.di.ExecutionMode;
import com.elo7.nightfall.di.Nightfall;
import com.elo7.nightfall.di.NightfallApplication;

public class NightfallRiposte {

	@Nightfall(
			value = ExecutionMode.BATCH,
			scanPackages = {"com.elo7.nightfall.riposte.task", "com.elo7.nightfall.riposte.batch"})
	public static class Batch {

		public static void main(String[] args) {
			NightfallApplication.run(NightfallRiposte.Batch.class, args);
		}
	}

	@Nightfall(
			value = ExecutionMode.STREAM,
			scanPackages = {"com.elo7.nightfall.riposte.task", "com.elo7.nightfall.riposte.stream"})
	public static class Stream {

		public static void main(String[] args) {
			NightfallApplication.run(NightfallRiposte.Stream.class, args);
		}
	}
}
