package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.util.EntityAccessRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements EntityAccessRenderState {

    @Unique
    private Entity subtleEffects$entity;
    @Unique
    private float subtleEffects$partialTick;

    @Override
    public Entity subtleEffects$getEntity() {
        return subtleEffects$entity;
    }

    @Override
    public void subtleEffects$setEntity(Entity entity) {
        subtleEffects$entity = entity;
    }

    @Override
    public float getPartialTick() {
        return subtleEffects$partialTick;
    }

    @Override
    public void setPartialTick(float partialTick) {
        subtleEffects$partialTick = partialTick;
    }
}
