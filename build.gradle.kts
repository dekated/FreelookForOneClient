plugins {
    id("net.fabricmc.fabric-loom-remap") version("1.17.+")
    id("ploceus") version("1.17.+")
}

group = "dev.dekated"
version = "mod_version"()

ploceus {
    setIntermediaryGeneration(2)
}

java.toolchain {
    // OneConfig 1.2.7 classes are Java 25 (class version 69), so compile with a
    // JDK 25+ (JDK 26 here) but target release 25 so the output runs on
    // OneClient's Java 25 runtime.
    languageVersion = JavaLanguageVersion.of(26)
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

configurations.configureEach {
    exclude(group = "org.lwjgl.lwjgl")
}

repositories {
    maven("https://repo.polyfrost.org/releases")
    maven("https://maven.cloverclient.com/releases")
    google()
}

dependencies {
    minecraft("com.mojang:minecraft:${"minecraft_version"()}")
    mappings(loom.layered {
        mappings(ploceus.featherMappings("feather_version"()))
    })

    modImplementation("net.fabricmc:fabric-loader:${"fabric_version"()}")
    ploceus.dependOsl("osl_version"())

    modImplementation("org.polyfrost.oneconfig:${"minecraft_version"()}-ornithe:${"oneconfig_version"()}")
    for (module in arrayOf("commands", "config", "config-impl", "events", "internal", "ui", "utils", "hud")) {
        implementation("org.polyfrost.oneconfig:$module:${"oneconfig_version"()}")
    }
}

tasks.processResources {
    val v = project.version
    inputs.property("version", v)

    filesMatching("fabric.mod.json") {
        expand("version" to v)
    }
}

operator fun String.invoke() = rootProject.providers.gradleProperty(this).orNull
    ?: error("Property $this not found")
