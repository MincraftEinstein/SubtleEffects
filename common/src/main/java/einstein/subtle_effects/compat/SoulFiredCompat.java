package einstein.subtle_effects.compat;

import it.crystalnest.soul_fire_d.api.FireManager;
import it.crystalnest.soul_fire_d.api.type.FireTyped;
import net.minecraft.world.entity.Entity;

public class SoulFiredCompat {

    public static boolean isOnSoulFire(Entity entity) {
        return ((FireTyped) entity).getFireType().equals(FireManager.SOUL_FIRE_TYPE);
    }
}
