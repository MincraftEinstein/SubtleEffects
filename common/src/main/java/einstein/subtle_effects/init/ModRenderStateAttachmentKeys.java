package einstein.subtle_effects.init;

import einstein.subtle_effects.client.renderer.ParticleBoundingBoxesRenderer;
import einstein.subtle_effects.util.RenderStateAttachmentAccessor;
import org.joml.Vector3f;

import java.util.List;

public class ModRenderStateAttachmentKeys {

    public static final RenderStateAttachmentAccessor.Key<String> STRING_UUID = new RenderStateAttachmentAccessor.Key<>("string_uuid");
    public static final RenderStateAttachmentAccessor.Key<Boolean> IS_SLEEPING = new RenderStateAttachmentAccessor.Key<>("is_sleeping");
    public static final RenderStateAttachmentAccessor.Key<Float> SOLAR_SYSTEM_SPIN = new RenderStateAttachmentAccessor.Key<>("solar_system_spin");
    public static final RenderStateAttachmentAccessor.Key<Boolean> IS_INVISIBLE = new RenderStateAttachmentAccessor.Key<>("is_invisible");
    public static final RenderStateAttachmentAccessor.Key<List<ParticleBoundingBoxesRenderer.RenderState>> PARTICLE_BOUNDING_BOXES = new RenderStateAttachmentAccessor.Key<>("particle_bounding_boxes");
    public static final RenderStateAttachmentAccessor.Key<Vector3f> COLOR = new RenderStateAttachmentAccessor.Key<>("color");
    public static final RenderStateAttachmentAccessor.Key<Boolean> IS_PARTY_PARROT = new RenderStateAttachmentAccessor.Key<>("is_party_parrot");
    public static final RenderStateAttachmentAccessor.Key<Integer> TICK_COUNT = new RenderStateAttachmentAccessor.Key<>("tick_count");
    public static final RenderStateAttachmentAccessor.Key<Integer> ENTITY_ID = new RenderStateAttachmentAccessor.Key<>("entity_id");
    public static final RenderStateAttachmentAccessor.Key<Float> HEALTH_PERCENTAGE = new RenderStateAttachmentAccessor.Key<>("health_percentage");
}
