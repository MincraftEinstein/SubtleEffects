package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticleRenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;

public abstract class BaseWaterfallParticle extends TextureSheetParticle {

    private final BlockPos.MutableBlockPos pos;
    private final LifetimeAlpha lifetimeAlpha;

    protected BaseWaterfallParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, LifetimeAlpha lifetimeAlpha) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetimeAlpha = lifetimeAlpha;
        alpha = lifetimeAlpha.startAlpha();
        pos = BlockPos.containing(x, y, z).mutable();

        if (ModConfigs.ENVIRONMENT.waterfalls.randomizeWaterfallParticleRotation) {
            roll = random.nextInt();
            oRoll = roll;
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        super.render(buffer, renderInfo, partialTicks);
        alpha = lifetimeAlpha.currentAlphaForAge(age, lifetime, partialTicks);
    }

    @Override
    public void tick() {
        super.tick();
        pos.set(x, y, z);

        if (onGround) {
            remove();
            return;
        }

        if (!level.getFluidState(pos).isEmpty()) {
            remove();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ModParticleRenderTypes.getBlendedOrTransparent();
    }
}
