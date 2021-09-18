package dev.quantumfusion.bettersleepplus;

import dev.quantumfusion.bettersleepplus.network.DynamicTimeChangePacket;
import net.minecraft.world.level.ServerWorldProperties;

import static dev.quantumfusion.bettersleepplus.BSPMath.*;

public class TimeInfo {
	private final ServerWorldProperties w;
	public int timeOfDay;
	public long time;
	public WeatherInfo currentWeatherInfo;
	private DynamicTimeChangePacket packet = null;

	public TimeInfo(ServerWorldProperties w) {
		this.w = w;
		this.timeOfDay = (int) this.w.getTimeOfDay();
		this.currentWeatherInfo = WeatherInfo.CLEAR;
	}

	public void update() {
		int clearTime = w.getClearWeatherTime();
		int rainTime = w.getRainTime();
		int thunderTime = w.getThunderTime();
		boolean raining = w.isRaining();
		boolean thundering = w.isThundering();
		this.timeOfDay = (int) w.getTimeOfDay();
		this.time = w.getTime();

		this.currentWeatherInfo = WeatherInfo.calcCurrentWeather(clearTime, raining, thundering);
		WeatherInfo.update(currentWeatherInfo, clearTime, rainTime, thunderTime);
	}

	public DynamicTimeChangePacket getPacket(boolean dayLightCycle) {
		if (packet == null) {
			packet = new DynamicTimeChangePacket(time, timeOfDay, dayLightCycle);
		}

		packet.update(time, timeOfDay);
		return packet;
	}

	public void tick(int amount, boolean dayLightCycle) {
		w.setTime(w.getTime() + amount);
		if (dayLightCycle) {
			w.setTimeOfDay((w.getTimeOfDay() + amount) % DAY_LENGTH);
		}
		WeatherInfo.appendAll(w, amount);
	}

	public int calcTicksUntilAwake() {
		long timeUntilChange = currentWeatherInfo.timeUntilChange;
		return (int) switch (currentWeatherInfo) {
			case CLEAR -> Math.min(getTicksTo(timeOfDay, CLEAR_AWAKE_TIME), timeUntilChange);
			case RAIN -> Math.min(getTicksTo(timeOfDay, RAIN_AWAKE_TIME), timeUntilChange);
			case THUNDER -> timeUntilChange;
		};
	}

}
