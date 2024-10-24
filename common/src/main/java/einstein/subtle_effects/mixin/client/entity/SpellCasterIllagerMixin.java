package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpellcasterIllager.class)
public class SpellCasterIllagerMixin {

    @WrapOperation(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;ENTITY_EFFECT:Lnet/minecraft/core/particles/ParticleType;"))
    private ParticleType<ColorParticleOption> replaceEntityEffect(Operation<ParticleType<ColorParticleOption>> original) {
        if (ModConfigs.ENTITIES.replaceSpellCasterParticles) {
            return ModParticles.SPELL_CASTER_MAGIC.get();
        }
        return original.call();
    }
}
