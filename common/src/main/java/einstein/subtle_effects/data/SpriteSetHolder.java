package einstein.subtle_effects.data;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SpriteSetHolder implements Supplier<ParticleEngine.MutableSpriteSet> {

    private final ResourceLocation id;
    @Nullable
    private ParticleEngine.MutableSpriteSet spriteSet;
    private boolean referencesPreExisting = false;

    public SpriteSetHolder(ResourceLocation id) {
        this.id = id;
    }

    public void set(ParticleEngine.MutableSpriteSet spriteSet, boolean referencesPreExisting) {
        this.spriteSet = spriteSet;
        this.referencesPreExisting = referencesPreExisting;
    }

    @Override
    public ParticleEngine.MutableSpriteSet get() {
        if (spriteSet == null) {
            throw new IllegalStateException("No sprite set is set for sprite set holder '" + id + "'");
        }

        return spriteSet;
    }

    public boolean referencesPreExisting() {
        return referencesPreExisting;
    }
}
