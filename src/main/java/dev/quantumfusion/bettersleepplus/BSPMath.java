package dev.quantumfusion.bettersleepplus;

public class BSPMath {
	public static final int DAY_LENGTH = 24000;
	public static final int CLEAR_AWAKE_TIME = 23460;
	public static final int RAIN_AWAKE_TIME = 23992;

	public static int calcStepPerTick(int multiplier, double curveAggression, double playerPercentage) {
		return (int) (multiplier * createCurve(curveAggression, playerPercentage));
	}

	public static int calcSecondsRemaining(int stepPerTick, TimeInfo info, int tps) {
		return (int) Math.ceil((info.calcTicksUntilAwake() / (double) stepPerTick) / tps);
	}

	private static double createCurve(double curveAggression, double playerPercentage) {
		return ((curveAggression * playerPercentage) / ((((curveAggression * playerPercentage) * 2) - (curveAggression - playerPercentage)) + 1));
	}


	public static int getTicksTo(int timeOfDay, int targetTimeOfDay) {
		if (timeOfDay > targetTimeOfDay)
			return targetTimeOfDay + DAY_LENGTH - timeOfDay;

		return targetTimeOfDay - timeOfDay;
	}


}
