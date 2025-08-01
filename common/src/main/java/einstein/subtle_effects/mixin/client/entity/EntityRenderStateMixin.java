package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.util.EntityRenderStateAccessor;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements EntityRenderStateAccessor {

    @Unique
    private boolean subtleEffects$isSleeping;

    @Unique
    private float subtleEffects$solarSystemSpin;

    @Unique
    private boolean subtleEffects$shouldRenderSolarSystem;

    @Override
    public boolean isSleeping() {
        return subtleEffects$isSleeping;
    }

    @Override
    public void setSleeping(boolean sleeping) {
        subtleEffects$isSleeping = sleeping;
    }

    @Override
    public float getSolarSystemSpin() {
        return subtleEffects$solarSystemSpin;
    }

    @Override
    public void setSolarSystemSpin(float solarSystemSpin) {
        subtleEffects$solarSystemSpin = solarSystemSpin;
    }

    @Override
    public boolean shouldRenderSolarSystem() {
        return subtleEffects$shouldRenderSolarSystem;
    }

    @Override
    public void setShouldRenderSolarSystem(boolean shouldRenderSolarSystem) {
        subtleEffects$shouldRenderSolarSystem = shouldRenderSolarSystem;
    }
}
