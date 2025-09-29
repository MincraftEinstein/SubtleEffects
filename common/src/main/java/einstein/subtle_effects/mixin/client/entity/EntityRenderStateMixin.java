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

    @Unique
    private boolean subtleEffects$shouldRenderAnniversaryHat;

    @Override
    public boolean subtleEffects$isSleeping() {
        return subtleEffects$isSleeping;
    }

    @Override
    public void subtleEffects$setSleeping(boolean sleeping) {
        subtleEffects$isSleeping = sleeping;
    }

    @Override
    public float subtleEffects$getSolarSystemSpin() {
        return subtleEffects$solarSystemSpin;
    }

    @Override
    public void subtleEffects$setSolarSystemSpin(float solarSystemSpin) {
        subtleEffects$solarSystemSpin = solarSystemSpin;
    }

    @Override
    public boolean subtleEffects$shouldRenderSolarSystem() {
        return subtleEffects$shouldRenderSolarSystem;
    }

    @Override
    public void subtleEffects$setShouldRenderSolarSystem(boolean shouldRenderSolarSystem) {
        subtleEffects$shouldRenderSolarSystem = shouldRenderSolarSystem;
    }

    @Override
    public boolean subtleEffects$shouldRenderAnniversaryHat() {
        return subtleEffects$shouldRenderAnniversaryHat;
    }

    @Override
    public void subtleEffects$setShouldRenderAnniversaryHat(boolean shouldRenderAnniversaryHat) {
        subtleEffects$shouldRenderAnniversaryHat = shouldRenderAnniversaryHat;
    }
}
