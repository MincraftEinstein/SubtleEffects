package einstein.subtle_effects.data;

import net.minecraft.client.particle.ParticleEngine;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SpriteSetHolder implements Supplier<ParticleEngine.MutableSpriteSet> {

    @Nullable
    private ParticleEngine.MutableSpriteSet spriteSet;
    private boolean referencesPreExisting = false;

    public void set(ParticleEngine.MutableSpriteSet spriteSet, boolean referencesPreExisting) {
        this.spriteSet = spriteSet;
        this.referencesPreExisting = referencesPreExisting;
    }

    @Override
    public ParticleEngine.MutableSpriteSet get() {
        if (spriteSet == null) {
            spriteSet = MissingSpriteSet.INSTANCE;
        }

        return spriteSet;
    }

    public boolean referencesPreExisting() {
        return referencesPreExisting;
    }
}
