package einstein.subtle_effects.platform.services;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    Platform getPlatform();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default Environment getEnvironmentName() {
        return isDevelopmentEnvironment() ? Environment.DEVELOPMENT : Environment.PRODUCTION;
    }

    enum Platform {
        FABRIC("fabric"),
        FORGE("forge"),
        NEOFORGE("neoforge");

        private final String name;

        Platform(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public boolean isForgeLike() {
            return this == NEOFORGE || this == FORGE;
        }
    }

    enum Environment {
        DEVELOPMENT("development"),
        PRODUCTION("production");

        private final String name;

        Environment(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}