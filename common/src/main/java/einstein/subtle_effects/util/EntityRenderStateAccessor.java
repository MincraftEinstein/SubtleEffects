package einstein.subtle_effects.util;

public interface EntityRenderStateAccessor {

    boolean subtleEffects$isSleeping();

    void subtleEffects$setSleeping(boolean sleeping);

    float subtleEffects$getSolarSystemSpin();

    void subtleEffects$setSolarSystemSpin(float solarSystemSpin);

    boolean subtleEffects$shouldRenderSolarSystem();

    void subtleEffects$setShouldRenderSolarSystem(boolean shouldRenderSolarSystem);

    boolean subtleEffects$shouldRenderAnniversaryHat();

    void subtleEffects$setShouldRenderAnniversaryHat(boolean shouldRenderAnniversaryHat);
}
