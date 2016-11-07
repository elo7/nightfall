package com.elo7.nightfall.distcp;

import com.elo7.nightfall.di.NightfallApplication;
import com.elo7.nightfall.distcp.provider.DateWindow;

@DateWindow
public class NightfallDistcpJob {

	public static void main(String[] args) {
		NightfallApplication.run(NightfallDistcpJob.class, args);
	}
}
