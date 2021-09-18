package dev.quantumfusion.bettersleepplus.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.SleepManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin(SleepManager.class)
public class SleepOverrideMixin {

	@SuppressWarnings("OverwriteAuthorRequired")
	@Overwrite
	public boolean canSkipNight(int percentage) {
		return false;
	}

	@Inject(method = "update", at = @At(value = "RETURN"))
	private void updateInject(List<ServerPlayerEntity> players, CallbackInfoReturnable<Boolean> cir) {

	}


}
