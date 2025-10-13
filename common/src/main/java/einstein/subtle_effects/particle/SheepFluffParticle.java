package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.particle.option.SheepFluffParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.color.ColorLerper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public class SheepFluffParticle extends FeatherParticle {

    private final boolean isJeb;
    private final Entity sheep;

    protected SheepFluffParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites, SheepFluffParticleOptions options) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        int sheepId = options.sheepId();
        isJeb = options.isJeb();
        sheep = isJeb ? level.getEntity(sheepId) : null;
        gravity = 0.5F;

        if (!isJeb) {
            Util.setColorFromHex(this, ColorLerper.Type.SHEEP.getColor(options.color()));
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        if (isJeb) {
            Util.setColorFromHex(this, ColorLerper.getLerpedColor(ColorLerper.Type.SHEEP, sheep.tickCount));
        }
        super.render(buffer, renderInfo, partialTicks);
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SheepFluffParticleOptions> {

        @Override
        public Particle createParticle(SheepFluffParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new SheepFluffParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites, options);
        }
    }
}
