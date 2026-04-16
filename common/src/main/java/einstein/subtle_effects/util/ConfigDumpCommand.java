package einstein.subtle_effects.util;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import com.electronwill.nightconfig.toml.TomlParser;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.SubtleEffectsClient;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.entry.EntryDelegate;
import me.fzzyhmstrs.fzzy_config.validation.ValidatedField;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigDumpCommand {

    private static final Map<String, String> MANUAL_ALLOWED_VALUES = Util.make(new HashMap<>(), map -> {
        String blockList = "A list containing any block String ID. List can be empty";
        String entityList = "A list containing any entity String ID. List can be empty";
        String biomeList = "A list containing any biome String ID. List can be empty";

        map.put("general.particleCullingBlocklist", "A list containing any particle String ID. List can be empty");
        map.put("blocks.redstoneDustEmittingBlocks", blockList);
        map.put("blocks.glowstoneDustEmittingBlocks", blockList);
        map.put("blocks.amethystSparkleEmittingBlocks", blockList);
        // TODO add links to where ever color objects and ingredient objects get explained
        map.put("blocks.eyeColors", "A list of lists, each sublist must contain the String ID of an Eye of Ender either from vanilla or from End Remastered, and a color object");
        map.put("blocks.fallingBlocks.dustyBlocks", "A list containing any block String ID, however non falling blocks will not be affected in average gameplay. List can be empty");
        map.put("entities.humanoids.frostyBreath.additionalBiomes", biomeList);
        map.put("entities.burning.entityBlocklist", entityList);
        map.put("entities.splashes.entityBlocklist", entityList);
        map.put("environment.biomes.mushroomSporeBiomes", biomeList);
        map.put("environment.biomes.pollenBiomes", biomeList);
        map.put("environment.biomes.sculkDustBiomes", biomeList);
        map.put("environment.geysers.flameGeyserSpawnableBlocks", blockList);
        map.put("environment.geysers.smokeGeyserSpawnableBlocks", blockList);
        map.put("environment.geysers.bubbleGeyserSpawnableBlocks", blockList);
        map.put("environment.fireflies.dimensionBlocklist", "A list containing any dimension String ID. List can be empty");
        map.put("environment.fireflies.biomesBlocklist", biomeList);
        map.put("environment.fireflies.biomesAllowlist", biomeList);
        map.put("environment.fireflies.spawnableBlocks", "A list containing any block String ID, however solid blocks will be ignored. List can be empty");
        map.put("environment.fireflies.habitatBiomes", biomeList);
        // TODO add links to where ever color objects and ingredient objects get explained
        map.put("items.itemRarity.colorOverrides", "A list of lists, each sublist must contain an ingredient object and a color object");
    });

    public static int execute(Player player) {
        try {
            Path configPath = ConfigApiJava.platform().configDir().toPath().resolve(SubtleEffects.MOD_ID);
            Path path = configPath.resolve("dump");
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }

            for (Field field : ModConfigs.class.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    if (field.get(null) instanceof Config config) {
                        writeConfigFile(path, configPath, config);
                    }
                }
            }

            SubtleEffectsClient.sendSystemMsg(player, Component.literal("Successfully dumped configs"));
            return 1;
        }
        catch (Exception e) {
            SubtleEffectsClient.sendSystemMsg(player, Component.literal("Failed to dump configs"));
            SubtleEffects.LOGGER.error("Failed to dump configs", e);
            return 0;
        }
    }

    private static void writeConfigFile(Path path, Path configPath, Config config) throws Exception {
        String configName = config.getName();
        Path filePath = path.resolve(configName + ".mdx");
        Files.deleteIfExists(filePath);
        Class<?> configClass = config.getClass();
        CommentedConfig commentedConfig = new TomlParser().parse(configPath.resolve(configName + ".toml"), FileNotFoundAction.THROW_ERROR, StandardCharsets.UTF_8);
        StringBuilder builder = new StringBuilder()
                .append("---")
                .append("\ntitle: ").append(getName(configName))
                .append("\n---")
                .append("\n\n**File name:** `").append(configName).append(".toml`");

        List<Field> sections = new ArrayList<>();
        createTable(builder, () -> fillTableRows(config, configClass, commentedConfig, sections, configName, builder));

        printSections(configName, config, sections, builder, commentedConfig);
        Files.writeString(filePath, builder, StandardCharsets.UTF_8);
    }

    private static void printSections(String path, Object instance, List<Field> sections, StringBuilder builder, CommentedConfig commentedConfig) throws Exception {
        for (Field section : sections) {
            Object fieldValue = section.get(instance);
            String sectionName = section.getName();
            String sectionPath = path + "." + sectionName;

            builder.append("\n### ").append(getName(sectionPath));
            createTable(builder, () -> {
                List<Field> subSections = new ArrayList<>();
                CommentedConfig sectionConfig = commentedConfig.get(sectionName);
                fillTableRows(fieldValue, fieldValue.getClass(), sectionConfig, subSections, sectionPath, builder);
                printSections(sectionPath, fieldValue, subSections, builder, sectionConfig);
            });
        }
    }

    private static void fillTableRows(Object instance, Class<?> configClass, CommentedConfig commentedConfig, List<Field> sections, String path, StringBuilder builder) throws Exception {
        for (Field field : configClass.getDeclaredFields()) {
            String fieldName = field.getName();

            if (commentedConfig.contains(fieldName)) {
                Object fieldValue = field.get(instance);
                if (fieldValue instanceof ConfigSection) {
                    sections.add(field);
                    continue;
                }

                String fieldPath = path + "." + fieldName;
                builder.append("\n    <tr>")
                        .append("\n        <td>").append(getName(fieldPath)).append("</td>")
                        .append("\n        <td>").append(getDescription(fieldPath)).append("</td>")
                        .append("\n        <td>").append(getDefaultValue(fieldValue, fieldPath)).append("</td>")
                        .append("\n        <td>").append(getAllowedValues(fieldValue, fieldPath)).append("</td>")
                        .append("\n        <td>").append(fieldName).append("</td>")
                        .append("\n    </tr>");
            }
        }
    }

    private static void createTable(StringBuilder builder, RunnableWithException runnable) throws Exception {
        builder.append("\n\n<table>")
                .append("\n    <tr>")
                .append("\n        <th>Name</th>")
                .append("\n        <th>Description</th>")
                .append("\n        <th>Default Value</th>")
                .append("\n        <th>Allowed Values</th>")
                .append("\n        <th>ID</th>")
                .append("\n    </tr>");
        runnable.run();
        builder.append("\n</table>\n");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static String getDefaultValue(Object object, String path) {
        if (object instanceof ValidatedField validated) {
            return "```" + removeTrailingDecimal(validated.serialize(validated.getDefault()).get().toString()) + "```";
        }
        else if (object instanceof Boolean || object instanceof Enum<?>) {
            return "`" + object + "`";
        }

        SubtleEffects.LOGGER.warn("Unknown config type for default value: {}", path);
        return object.toString();
    }

    private static String getAllowedValues(Object object, String path) throws Exception {
        Class<?> clazz = object.getClass();
        Class<?> superclass = clazz.getSuperclass();

        // noinspection all
        while (object instanceof EntryDelegate) {
            object = callGetter(object, "getDelegate", superclass);
            clazz = object.getClass();
            superclass = clazz.getSuperclass();
        }

        if (object instanceof ValidatedField<?> validatedField) {
            object = validatedField.get();

            if (object instanceof Number) {
                String maxValue = removeTrailingDecimal(callGetter(validatedField, "getMaxValue", superclass).toString());
                String minValue = removeTrailingDecimal(callGetter(validatedField, "getMinValue", superclass).toString());
                return removeClassPackage(((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0]
                        .getTypeName()) + " (`" + minValue + "` to `" + maxValue + "`)";
            }
        }

        if (object instanceof Boolean) {
            return "Boolean (`true` or `false`)";
        }
        else if (object instanceof Enum<?> anEnum) {
            StringBuilder enumList = new StringBuilder();
            enumList.append("<ul>");

            for (Field field : anEnum.getDeclaringClass().getDeclaredFields()) {
                if (field.isEnumConstant()) {
                    enumList.append("<li>`").append(field.getName()).append("`</li>");
                }
            }

            enumList.append("</ul>");
            return enumList.toString();
        }
        else if (MANUAL_ALLOWED_VALUES.containsKey(path)) {
            return MANUAL_ALLOWED_VALUES.get(path);
        }

        SubtleEffects.LOGGER.warn("Unknown config type for allowed values: {}", path);
        return object.toString();
    }

    private static Object callGetter(Object object, String methodName, Class<?> clazz) throws Exception {
        Method method = clazz.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(object);
    }

    private static String removeTrailingDecimal(String s) {
        if (s.endsWith(".0")) {
            int i = s.lastIndexOf(".0");
            return s.substring(0, i);
        }
        return s;
    }

    private static String removeClassPackage(String s) {
        if (s.contains(".")) {
            int i = s.lastIndexOf(".");
            return s.substring(++i);
        }
        return s;
    }

    private static String getDescription(String path) {
        String descPath = path + ".desc";
        String s = getTranslation(descPath);
        if (s.equals(ModConfigs.BASE_KEY + descPath) || s.isBlank()) {
            return "";
        }

        s = s.replace("\n", "<br/>");
        while (s.contains("§")) {
            int i = s.indexOf("§");
            s = s.substring(0, i) + s.substring(i + 2);
        }
        return s;
    }

    private static String getName(String path) {
        String s = getTranslation(path);
        if (s.equals(ModConfigs.BASE_KEY + path) || s.isBlank()) {
            SubtleEffects.LOGGER.warn("Found untranslated string: {}", s);
        }
        return s;
    }

    private static String getTranslation(String path) {
        return Component.translatable(ModConfigs.BASE_KEY + path).getString();
    }

    @FunctionalInterface
    private interface RunnableWithException {

        void run() throws Exception;
    }
}
