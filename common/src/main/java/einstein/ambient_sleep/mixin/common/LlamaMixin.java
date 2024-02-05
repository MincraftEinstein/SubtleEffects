package einstein.ambient_sleep.mixin.common;

import einstein.ambient_sleep.util.Util;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Llama.class)
public abstract class LlamaMixin extends AbstractChestedHorse {

    protected LlamaMixin(EntityType<? extends AbstractChestedHorse> type, Level level) {
        super(type, level);
    }

    @Redirect(method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/Llama;calculateFallDamage(FF)I"))
    private int calculateFallDamage(Llama llama, float distance, float damageMultiplier) {
        int fallDamage = calculateFallDamage(distance, damageMultiplier);
        Util.spawnFallDustClouds(llama, distance, fallDamage);
        return fallDamage;
    }
}
