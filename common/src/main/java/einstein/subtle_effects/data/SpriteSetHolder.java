package einstein.subtle_effects.data;

import net.minecraft.client.particle.ParticleResources;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SpriteSetHolder implements Supplier<ParticleResources.MutableSpriteSet> {

    private final Identifier id;
    @Nullable
    private ParticleResources.MutableSpriteSet spriteSet;
    private boolean referencesPreExisting = false;

    public SpriteSetHolder(Identifier id) {
        this.id = id;
    }

    public void set(ParticleResources.MutableSpriteSet spriteSet, boolean referencesPreExisting) {
        this.spriteSet = spriteSet;
        this.referencesPreExisting = referencesPreExisting;
    }

    @Override
    public ParticleResources.MutableSpriteSet get() {
        if (spriteSet == null) {
            throw new IllegalStateException("No sprite set is set for sprite set holder '" + id + "'");
        }

        return spriteSet;
    }

    public boolean referencesPreExisting() {
        return referencesPreExisting;
    }
}
