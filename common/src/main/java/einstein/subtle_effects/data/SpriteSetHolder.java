package einstein.subtle_effects.data;

import net.minecraft.client.particle.ParticleResources;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SpriteSetHolder implements Supplier<ParticleResources.MutableSpriteSet> {

    @Nullable
    private ParticleResources.MutableSpriteSet spriteSet;
    private boolean referencesPreExisting = false;

    public void set(ParticleResources.MutableSpriteSet spriteSet, boolean referencesPreExisting) {
        this.spriteSet = spriteSet;
        this.referencesPreExisting = referencesPreExisting;
    }

    @Override
    public ParticleResources.MutableSpriteSet get() {
        if (spriteSet == null) {
            spriteSet = MissingSpriteSet.INSTANCE;
        }

        return spriteSet;
    }

    public boolean referencesPreExisting() {
        return referencesPreExisting;
    }
}
