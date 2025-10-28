package einstein.subtle_effects.init;

import einstein.subtle_effects.util.RenderStateAttachmentAccessor;

public class ModRenderStateAttachmentKeys {

    public static final RenderStateAttachmentAccessor.Key<String> STRING_UUID = new RenderStateAttachmentAccessor.Key<>("string_uuid");
    public static final RenderStateAttachmentAccessor.Key<Boolean> IS_SLEEPING = new RenderStateAttachmentAccessor.Key<>("is_sleeping");
    public static final RenderStateAttachmentAccessor.Key<Float> SOLAR_SYSTEM_SPIN = new RenderStateAttachmentAccessor.Key<>("solar_system_spin");
    public static final RenderStateAttachmentAccessor.Key<Boolean> IS_INVISIBLE = new RenderStateAttachmentAccessor.Key<>("is_invisible");
}
