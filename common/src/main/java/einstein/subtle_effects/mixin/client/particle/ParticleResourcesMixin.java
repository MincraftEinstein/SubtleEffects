package einstein.subtle_effects.mixin.client.particle;

import einstein.subtle_effects.init.ModSpriteSets;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ParticleResources.class)
public class ParticleResourcesMixin {

    @Shadow
    @Final
    private Map<ResourceLocation, ParticleResources.MutableSpriteSet> spriteSets;

    @Inject(method = "registerProviders", at = @At("TAIL"))
    private void registerProviders(CallbackInfo ci) {
        spriteSets.putAll(ModSpriteSets.REGISTERED);
    }
}
