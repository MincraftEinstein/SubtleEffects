package einstein.subtle_effects.util;

import net.minecraft.world.entity.Entity;

public interface EntityAccessRenderState {

    Entity subtleEffects$getEntity();

    void subtleEffects$setEntity(Entity entity);

    float getPartialTick();

    void setPartialTick(float partialTick);
}
