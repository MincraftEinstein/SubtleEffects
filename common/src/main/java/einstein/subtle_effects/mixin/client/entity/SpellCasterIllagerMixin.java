package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpellcasterIllager.class)
public class SpellCasterIllagerMixin {

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;ENTITY_EFFECT:Lnet/minecraft/core/particles/ParticleType;"))
    private ParticleType<ColorParticleOption> replaceEntityEffect() {
        if (ModConfigs.INSTANCE.replaceSpellCasterParticles.get()) {
            return ModParticles.SPELL_CASTER_MAGIC.get();
        }
        return ParticleTypes.ENTITY_EFFECT;
    }
}
