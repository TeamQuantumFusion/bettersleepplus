package dev.quantumfusion.bettersleepplus.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

public class DynamicTimeChangePacket extends WorldTimeUpdateS2CPacket {
	private long time;
	private long timeOfDay;

	public DynamicTimeChangePacket(long time, long timeOfDay, boolean daylightCycle) {
		super(time, timeOfDay, daylightCycle);
		this.time = super.getTime();
		this.timeOfDay = super.getTimeOfDay();
	}

	public void update(long time, long timeOfDay) {
		this.time = time;
		this.timeOfDay = timeOfDay;
	}

	public DynamicTimeChangePacket(PacketByteBuf packetByteBuf) {
		super(packetByteBuf);
		this.time = packetByteBuf.readLong();
		this.timeOfDay = packetByteBuf.readLong();
	}

	public void write(PacketByteBuf buf) {
		buf.writeLong(this.time);
		buf.writeLong(this.timeOfDay);
	}

	public long getTime() {
		return this.time;
	}

	public long getTimeOfDay() {
		return this.timeOfDay;
	}
}
