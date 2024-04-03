package einstein.ambient_sleep.client.particle;

import einstein.ambient_sleep.AmbientSleep;
import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemRarityParticle extends TextureSheetParticle {

    private static final TextColor WHITE_TEXT = TextColor.fromLegacyFormat(ChatFormatting.WHITE);

    protected ItemRarityParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ItemStack stack) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        int color = 0xFF000000 | getItemColor(stack).getValue();
        setColor((color >> 16) / 255F, (color >> 8) / 255F, color / 255F);
        pickSprite(sprites);
        lifetime = Math.max(5, random.nextInt(7));
        xd = 0;
        zd = 0;
    }

    private static TextColor getItemColor(ItemStack stack) {
        if (ModConfigs.INSTANCE.itemRarityParticleColor.get() == ModConfigs.ItemRarityColorType.NAME_COLOR) {
            TextColor nameColor = stack.getHoverName().getStyle().getColor();
            if (nameColor != null && !nameColor.equals(WHITE_TEXT)) {
                return nameColor;
            }
        }

        TextColor rarityColor = TextColor.fromLegacyFormat(stack.getRarity().color);
        if (rarityColor != null) {
            return rarityColor;
        }

        AmbientSleep.LOGGER.error("Failed to get text color for item: " + stack.getDisplayName());
        return WHITE_TEXT;
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
