package dev.quantumfusion.bettersleepplus.mixin;

import dev.quantumfusion.bettersleepplus.BSPMath;
import dev.quantumfusion.bettersleepplus.TimeInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.ServerWorldProperties;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {

	@Shadow
	@Final
	private ServerWorldProperties worldProperties;
	private TimeInfo info;
	@Shadow
	@Final
	private List<ServerPlayerEntity> players;
	@Shadow
	@Final
	private MinecraftServer server;

	protected ServerWorldMixin(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey, DimensionType dimensionType, Supplier<Profiler> supplier, boolean bl, boolean bl2, long l) {
		super(mutableWorldProperties, registryKey, dimensionType, supplier, bl, bl2, l);
	}

	@Shadow
	@NotNull
	public abstract MinecraftServer getServer();

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getInt(Lnet/minecraft/world/GameRules$Key;)I"))
	private void tickInject(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		if (info == null) {
			info = new TimeInfo(worldProperties);
		}

		int clearTime = worldProperties.getClearWeatherTime();
		int rainTime = worldProperties.getRainTime();
		int thunderTime = worldProperties.getThunderTime();
		boolean raining = worldProperties.isRaining();
		boolean thundering = worldProperties.isThundering();
		boolean dayLightCycle = server.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE);

		int stepPerTick = BSPMath.calcStepPerTick(59, 0.5, 1);

		info.update();
		info.tick(stepPerTick, dayLightCycle);
		server.getPlayerManager().sendToDimension(info.getPacket(dayLightCycle), getRegistryKey());


		for (ServerPlayerEntity player : players) {
			player.sendMessage(Text.of("RESULT: " + BSPMath.calcSecondsRemaining(stepPerTick,info,20) + " T: " + info.timeOfDay + " C: " + clearTime + " R: " + rainTime + " T: " + thunderTime + " RTB: " + raining + " " + thundering), true);
		}
	}


}
