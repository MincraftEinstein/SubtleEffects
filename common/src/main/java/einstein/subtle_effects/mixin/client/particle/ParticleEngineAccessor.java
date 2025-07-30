package einstein.subtle_effects.mixin.client.particle;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ParticleEngine.class)
public interface ParticleEngineAccessor {

    @Accessor("RENDER_ORDER")
    static List<ParticleRenderType> getRenderOrder() {
        throw new AssertionError("Mixin did not apply!");
    }

    @Mutable
    @Accessor("RENDER_ORDER")
    static void setRenderOrder(List<ParticleRenderType> renderOrder) {
        throw new AssertionError("Mixin did not apply!");
    }
}
