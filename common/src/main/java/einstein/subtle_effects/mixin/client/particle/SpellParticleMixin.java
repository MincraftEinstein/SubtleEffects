package einstein.subtle_effects.mixin.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static einstein.subtle_effects.init.ModConfigs.GENERAL;

@Mixin(SpellParticle.class)
public abstract class SpellParticleMixin extends TextureSheetParticle {

    @Shadow
    private float originalAlpha;

    @Unique
    private boolean subtleEffects$isFirstFrame = true;

    public SpellParticleMixin(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTick) {
        if (subtleEffects$isFirstFrame) {
            subtleEffects$isFirstFrame = false;

            if (subtleEffects$isCloseToPlayer()) {
                setAlpha(GENERAL.potionParticleAlphaNearPlayer.get());
            }
            else if (alpha == 1 || alpha == 0) {
                setAlpha(GENERAL.potionParticleAlpha.get());
            }
        }
        super.render(consumer, camera, partialTick);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (subtleEffects$isCloseToPlayer()) {
            alpha = GENERAL.potionParticleAlphaNearPlayer.get();
            return;
        }

        alpha = Mth.lerp(0.05F, alpha, originalAlpha);
    }

    @Unique
    private boolean subtleEffects$isCloseToPlayer() {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        return player != null && player.getEyePosition().distanceToSqr(x, y, z) <= 3 && minecraft.options.getCameraType().isFirstPerson();
    }
}
