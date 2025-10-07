package einstein.subtle_effects.init;

import einstein.subtle_effects.util.EntityRenderStateAccessor;

public class ModRenderStateKeys {

    public static final EntityRenderStateAccessor.Key<String> STRING_UUID = new EntityRenderStateAccessor.Key<>("string_uuid");
    public static final EntityRenderStateAccessor.Key<Boolean> IS_SLEEPING = new EntityRenderStateAccessor.Key<>("is_sleeping");
    public static final EntityRenderStateAccessor.Key<Float> SOLAR_SYSTEM_SPIN = new EntityRenderStateAccessor.Key<>("solar_system_spin");
    public static final EntityRenderStateAccessor.Key<Boolean> IS_INVISIBLE = new EntityRenderStateAccessor.Key<>("is_invisible");
}
