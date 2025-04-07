package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `libs` extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final AdventureLibraryAccessors laccForAdventureLibraryAccessors = new AdventureLibraryAccessors(owner);
    private final HutoolLibraryAccessors laccForHutoolLibraryAccessors = new HutoolLibraryAccessors(owner);
    private final JacksonLibraryAccessors laccForJacksonLibraryAccessors = new JacksonLibraryAccessors(owner);
    private final KotlinLibraryAccessors laccForKotlinLibraryAccessors = new KotlinLibraryAccessors(owner);
    private final ScoreboardlibraryLibraryAccessors laccForScoreboardlibraryLibraryAccessors = new ScoreboardlibraryLibraryAccessors(owner);
    private final SpigotLibraryAccessors laccForSpigotLibraryAccessors = new SpigotLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

        /**
         * Creates a dependency provider for book (xyz.upperlevel.spigot.book:spigot-book-api)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBook() {
            return create("book");
    }

        /**
         * Creates a dependency provider for decentholograms (com.github.decentsoftware-eu:decentholograms)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getDecentholograms() {
            return create("decentholograms");
    }

        /**
         * Creates a dependency provider for gui (com.huanmeng-qwq:Bukkit-Gui)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getGui() {
            return create("gui");
    }

        /**
         * Creates a dependency provider for httpclient (org.apache.httpcomponents:httpclient)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getHttpclient() {
            return create("httpclient");
    }

        /**
         * Creates a dependency provider for httpcore (org.apache.httpcomponents:httpcore)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getHttpcore() {
            return create("httpcore");
    }

        /**
         * Creates a dependency provider for jedis (redis.clients:jedis)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJedis() {
            return create("jedis");
    }

        /**
         * Creates a dependency provider for litecommands (dev.rollczi:litecommands-bukkit)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getLitecommands() {
            return create("litecommands");
    }

        /**
         * Creates a dependency provider for luckperms (net.luckperms:api)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getLuckperms() {
            return create("luckperms");
    }

        /**
         * Creates a dependency provider for mongodb (org.mongodb:mongo-java-driver)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMongodb() {
            return create("mongodb");
    }

        /**
         * Creates a dependency provider for mongojack (org.mongojack:mongojack)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMongojack() {
            return create("mongojack");
    }

        /**
         * Creates a dependency provider for moshi (com.squareup.moshi:moshi)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMoshi() {
            return create("moshi");
    }

        /**
         * Creates a dependency provider for narshorn (org.openjdk.nashorn:nashorn-core)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getNarshorn() {
            return create("narshorn");
    }

        /**
         * Creates a dependency provider for packetevents (com.github.retrooper.packetevents:spigot)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPacketevents() {
            return create("packetevents");
    }

        /**
         * Creates a dependency provider for papi (me.clip:placeholderapi)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPapi() {
            return create("papi");
    }

        /**
         * Creates a dependency provider for playerpoints (org.black_ixx:playerpoints)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPlayerpoints() {
            return create("playerpoints");
    }

        /**
         * Creates a dependency provider for protocollib (com.comphenix.protocol:ProtocolLib)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getProtocollib() {
            return create("protocollib");
    }

        /**
         * Creates a dependency provider for reflectionhelper (org.inventivetalent:reflectionhelper)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getReflectionhelper() {
            return create("reflectionhelper");
    }

        /**
         * Creates a dependency provider for slf4j (org.slf4j:slf4j-simple)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSlf4j() {
            return create("slf4j");
    }

        /**
         * Creates a dependency provider for websocket (org.java-websocket:Java-WebSocket)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getWebsocket() {
            return create("websocket");
    }

        /**
         * Creates a dependency provider for yamlassist (me.neznamy:yamlassist)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getYamlassist() {
            return create("yamlassist");
    }

    /**
     * Returns the group of libraries at adventure
     */
    public AdventureLibraryAccessors getAdventure() {
        return laccForAdventureLibraryAccessors;
    }

    /**
     * Returns the group of libraries at hutool
     */
    public HutoolLibraryAccessors getHutool() {
        return laccForHutoolLibraryAccessors;
    }

    /**
     * Returns the group of libraries at jackson
     */
    public JacksonLibraryAccessors getJackson() {
        return laccForJacksonLibraryAccessors;
    }

    /**
     * Returns the group of libraries at kotlin
     */
    public KotlinLibraryAccessors getKotlin() {
        return laccForKotlinLibraryAccessors;
    }

    /**
     * Returns the group of libraries at scoreboardlibrary
     */
    public ScoreboardlibraryLibraryAccessors getScoreboardlibrary() {
        return laccForScoreboardlibraryLibraryAccessors;
    }

    /**
     * Returns the group of libraries at spigot
     */
    public SpigotLibraryAccessors getSpigot() {
        return laccForSpigotLibraryAccessors;
    }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class AdventureLibraryAccessors extends SubDependencyFactory {

        public AdventureLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for api (net.kyori:adventure-api)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getApi() {
                return create("adventure.api");
        }

            /**
             * Creates a dependency provider for bukkit (net.kyori:adventure-platform-bukkit)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getBukkit() {
                return create("adventure.bukkit");
        }

            /**
             * Creates a dependency provider for bungee (net.kyori:adventure-platform-bungeecord)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getBungee() {
                return create("adventure.bungee");
        }

            /**
             * Creates a dependency provider for gson (net.kyori:adventure-text-serializer-gson)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGson() {
                return create("adventure.gson");
        }

            /**
             * Creates a dependency provider for nbt (net.kyori:adventure-nbt)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getNbt() {
                return create("adventure.nbt");
        }

    }

    public static class HutoolLibraryAccessors extends SubDependencyFactory {

        public HutoolLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (cn.hutool:hutool-core)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() {
                return create("hutool.core");
        }

            /**
             * Creates a dependency provider for crypto (cn.hutool:hutool-crypto)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCrypto() {
                return create("hutool.crypto");
        }

    }

    public static class JacksonLibraryAccessors extends SubDependencyFactory {

        public JacksonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for guava (com.fasterxml.jackson.datatype:jackson-datatype-guava)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGuava() {
                return create("jackson.guava");
        }

            /**
             * Creates a dependency provider for jdk8 (com.fasterxml.jackson.datatype:jackson-datatype-jdk8)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJdk8() {
                return create("jackson.jdk8");
        }

            /**
             * Creates a dependency provider for jsr310 (com.fasterxml.jackson.datatype:jackson-datatype-jsr310)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJsr310() {
                return create("jackson.jsr310");
        }

            /**
             * Creates a dependency provider for kotlin (com.fasterxml.jackson.module:jackson-module-kotlin)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getKotlin() {
                return create("jackson.kotlin");
        }

    }

    public static class KotlinLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public KotlinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for kotlin (org.jetbrains.kotlin:kotlin-stdlib)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() {
                return create("kotlin");
        }

            /**
             * Creates a dependency provider for test (org.jetbrains.kotlin:kotlin-test-junit)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getTest() {
                return create("kotlin.test");
        }

    }

    public static class ScoreboardlibraryLibraryAccessors extends SubDependencyFactory {

        public ScoreboardlibraryLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for 18 (net.megavex:scoreboard-library-v1_8_R3)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> get18() {
                return create("scoreboardlibrary.18");
        }

            /**
             * Creates a dependency provider for api (net.megavex:scoreboard-library-api)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getApi() {
                return create("scoreboardlibrary.api");
        }

            /**
             * Creates a dependency provider for impl (net.megavex:scoreboard-library-implementation)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getImpl() {
                return create("scoreboardlibrary.impl");
        }

            /**
             * Creates a dependency provider for kotlin (net.megavex:scoreboard-library-extra-kotlin)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getKotlin() {
                return create("scoreboardlibrary.kotlin");
        }

            /**
             * Creates a dependency provider for packetevents (net.megavex:scoreboard-library-packetevents)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getPacketevents() {
                return create("scoreboardlibrary.packetevents");
        }

    }

    public static class SpigotLibraryAccessors extends SubDependencyFactory {

        public SpigotLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for 8 (org.spigotmc:spigot)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> get8() {
                return create("spigot.8");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final AdventureVersionAccessors vaccForAdventureVersionAccessors = new AdventureVersionAccessors(providers, config);
        private final ScoreboardVersionAccessors vaccForScoreboardVersionAccessors = new ScoreboardVersionAccessors(providers, config);
        private final ServerVersionAccessors vaccForServerVersionAccessors = new ServerVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: book (1.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getBook() { return getVersion("book"); }

            /**
             * Returns the version associated to this alias: decentholograms (2.8.6)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getDecentholograms() { return getVersion("decentholograms"); }

            /**
             * Returns the version associated to this alias: gui (2.2.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getGui() { return getVersion("gui"); }

            /**
             * Returns the version associated to this alias: httpclient (4.4)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getHttpclient() { return getVersion("httpclient"); }

            /**
             * Returns the version associated to this alias: hutool (5.8.27)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getHutool() { return getVersion("hutool"); }

            /**
             * Returns the version associated to this alias: jackson (2.17.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJackson() { return getVersion("jackson"); }

            /**
             * Returns the version associated to this alias: jedis (2.9.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJedis() { return getVersion("jedis"); }

            /**
             * Returns the version associated to this alias: kotlin (2.0.0-RC3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getKotlin() { return getVersion("kotlin"); }

            /**
             * Returns the version associated to this alias: litecommands (3.4.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getLitecommands() { return getVersion("litecommands"); }

            /**
             * Returns the version associated to this alias: luckperms (5.4)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getLuckperms() { return getVersion("luckperms"); }

            /**
             * Returns the version associated to this alias: mongodb (3.12.11)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMongodb() { return getVersion("mongodb"); }

            /**
             * Returns the version associated to this alias: monjack (4.8.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMonjack() { return getVersion("monjack"); }

            /**
             * Returns the version associated to this alias: moshi (1.15.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMoshi() { return getVersion("moshi"); }

            /**
             * Returns the version associated to this alias: narshorn (15.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getNarshorn() { return getVersion("narshorn"); }

            /**
             * Returns the version associated to this alias: packetevents (2.3.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getPacketevents() { return getVersion("packetevents"); }

            /**
             * Returns the version associated to this alias: papi (2.11.6)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getPapi() { return getVersion("papi"); }

            /**
             * Returns the version associated to this alias: playerpoints (3.2.4)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getPlayerpoints() { return getVersion("playerpoints"); }

            /**
             * Returns the version associated to this alias: protocolLib (5.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getProtocolLib() { return getVersion("protocolLib"); }

            /**
             * Returns the version associated to this alias: reflectionhelper (1.15.2-SNAPSHOT)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getReflectionhelper() { return getVersion("reflectionhelper"); }

            /**
             * Returns the version associated to this alias: shadow (8.1.7)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getShadow() { return getVersion("shadow"); }

            /**
             * Returns the version associated to this alias: slf4j (1.7.30)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getSlf4j() { return getVersion("slf4j"); }

            /**
             * Returns the version associated to this alias: websocket (1.5.4)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getWebsocket() { return getVersion("websocket"); }

            /**
             * Returns the version associated to this alias: yamlassist (1.0.5)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getYamlassist() { return getVersion("yamlassist"); }

        /**
         * Returns the group of versions at versions.adventure
         */
        public AdventureVersionAccessors getAdventure() {
            return vaccForAdventureVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.scoreboard
         */
        public ScoreboardVersionAccessors getScoreboard() {
            return vaccForScoreboardVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.server
         */
        public ServerVersionAccessors getServer() {
            return vaccForServerVersionAccessors;
        }

    }

    public static class AdventureVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        public AdventureVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the version associated to this alias: adventure (4.17.0)
         * If the version is a rich version and that its not expressible as a
         * single version string, then an empty string is returned.
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("adventure"); }

            /**
             * Returns the version associated to this alias: adventure.platform (4.3.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getPlatform() { return getVersion("adventure.platform"); }

    }

    public static class ScoreboardVersionAccessors extends VersionFactory  {

        public ScoreboardVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: scoreboard.library (2.1.8)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getLibrary() { return getVersion("scoreboard.library"); }

    }

    public static class ServerVersionAccessors extends VersionFactory  {

        public ServerVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: server.8 (1.8.8-R0.1-SNAPSHOT)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> get8() { return getVersion("server.8"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for shadow to the plugin id 'io.github.goooler.shadow'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getShadow() { return createPlugin("shadow"); }

    }

}
