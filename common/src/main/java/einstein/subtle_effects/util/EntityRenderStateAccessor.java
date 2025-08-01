package einstein.subtle_effects.util;

public interface EntityRenderStateAccessor {

    boolean isSleeping();

    void setSleeping(boolean sleeping);

    float getSolarSystemSpin();

    void setSolarSystemSpin(float solarSystemSpin);

    boolean shouldRenderSolarSystem();

    void setShouldRenderSolarSystem(boolean shouldRenderSolarSystem);
}
