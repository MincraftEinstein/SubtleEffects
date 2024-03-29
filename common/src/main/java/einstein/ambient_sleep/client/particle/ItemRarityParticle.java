package einstein.ambient_sleep.client.particle;

import einstein.ambient_sleep.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class ItemRarityParticle extends TextureSheetParticle {

    protected ItemRarityParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ItemStack stack) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        Rarity rarity = stack.getRarity();
        Color color = new Color(rarity.color.getColor());
        setColor(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
        pickSprite(sprites);
        lifetime = Math.max(5, random.nextInt(7));
        xd = 0;
        zd = 0;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return Util.getLightColor(super.getLightColor(partialTick));
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<ItemParticleOption> {

        @Nullable
        @Override
        public Particle createParticle(ItemParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ItemRarityParticle(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed, type.getItem());
        }
    }
}
