package einstein.ambient_sleep.mixin;

import einstein.ambient_sleep.util.LoopingSpriteSet;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FabricSpriteProviderImpl.class)
public class FabricSpriteProviderImplMixin implements LoopingSpriteSet {

    @Shadow
    @Final
    private SpriteSet delegate;

    @Override
    public TextureAtlasSprite ambientSleep$nextSpite() {
        return ((LoopingSpriteSet) delegate).ambientSleep$nextSpite();
    }
}
