package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.particle.option.SheepFluffParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.DyeColor;

public class SheepFluffParticle extends FeatherParticle {

    private final int sheepId;
    private final boolean isJeb;
    private final Entity sheep;

    protected SheepFluffParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites, SheepFluffParticleOptions options) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        sheepId = options.sheepId();
        isJeb = options.isJeb();
        sheep = isJeb ? level.getEntity(sheepId) : null;
        gravity = 0.5F;

        if (!isJeb) {
            Util.setColorFromHex(this, Sheep.getColor(options.color()));
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        if (isJeb) {
            int speed = 25;
            int tickCount = sheep.tickCount;
            int i = tickCount / speed + sheepId;
            int length = DyeColor.values().length;
            float delta = ((float) (tickCount % speed) + partialTicks) / speed;
            int startColor = Sheep.getColor(DyeColor.byId(i % length));
            int endColor = Sheep.getColor(DyeColor.byId((i + 1) % length));
            Util.setColorFromHex(this, ARGB.lerp(delta, startColor, endColor));
        }
        super.render(buffer, renderInfo, partialTicks);
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SheepFluffParticleOptions> {

        @Override
        public Particle createParticle(SheepFluffParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SheepFluffParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites, options);
        }
    }
}
