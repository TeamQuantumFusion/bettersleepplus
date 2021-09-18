package dev.quantumfusion.bettersleepplus;

import net.minecraft.world.level.ServerWorldProperties;

import java.util.function.BiConsumer;

public enum WeatherInfo {
	CLEAR(0, true, ServerWorldProperties::setClearWeatherTime),
	RAIN(0, true, ServerWorldProperties::setRainTime),
	THUNDER(0, true, ServerWorldProperties::setThunderTime);

	public long timeUntilChange;
	public boolean active;
	public BiConsumer<ServerWorldProperties, Integer> set;

	WeatherInfo(long timeUntilChange, boolean active, BiConsumer<ServerWorldProperties, Integer> set) {
		this.timeUntilChange = timeUntilChange;
		this.active = active;
		this.set = set;
	}

	public static WeatherInfo calcCurrentWeather(long clearTime, boolean rain, boolean thunder) {
		if (clearTime > 0 || !rain)
			return CLEAR;

		if (thunder)
			return THUNDER;
		else
			return RAIN;
	}

	public static void update(WeatherInfo currentWeatherInfo, long clearTime, long rainTime, long thunderTime) {
		CLEAR.update(clearTime, currentWeatherInfo);
		RAIN.update(rainTime, currentWeatherInfo);
		THUNDER.update(thunderTime, currentWeatherInfo);
	}

	public static void appendAll(ServerWorldProperties prop, int amount) {
		for (WeatherInfo value : values()) {
			value.append(prop, amount);
		}
	}

	private void update(long timeUntilChange, WeatherInfo currentWeatherInfo) {
		this.timeUntilChange = timeUntilChange;
		this.active = currentWeatherInfo == this;
	}

	public void append(ServerWorldProperties prop, int amount) {
		set.accept(prop, Math.max((int) (timeUntilChange - amount), 0));
	}
}
