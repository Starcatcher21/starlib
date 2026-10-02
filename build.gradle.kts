import gg.meza.stonecraft.mod
import org.gradle.internal.configuration.problems.projectPathFrom

plugins {
    id("gg.meza.stonecraft")
    `maven-publish`
}

loom {
    version = project.mod.version
    enableTransitiveAccessWideners = false
}

modSettings {
    clientOptions {
        fov = 110
        guiScale = 3
        narrator = false
        darkBackground = true
        musicVolume = 0.0
    }

    variableReplacements =
        mapOf(
            "minecraftVersionVirtual" to stonecutter.current.version,
            "forgeLoaderVersion" to
                if (project.mod.isForge) {
                    project.mod
                        .prop("forge_version")
                        .substringAfter("-")
                        .substringBefore(".")
                } else {
                    ""
                },
        )
}

java {
    val javaVersion = when {
        // 1.21.1 targets Java 21
        stonecutter.current.project.startsWith("1.21.1") -> 21
        // 26.2 and 26.3 both target Java 25
        stonecutter.current.project.startsWith("26.2") -> 25
        // Fallback (should not normally be hit)
        else -> 21
    }
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
}


dependencies {
    val archApiVersion = project.mod.prop("architectury_api_version")

    when {
        project.mod.isFabric -> {
            implementation("dev.architectury:architectury-fabric:$archApiVersion")
        }
        stonecutter.current.project.endsWith("-neoforge") -> {
            implementation("dev.architectury:architectury-neoforge:$archApiVersion")
        }
    }
}

publishing {
    publications {
        // Create a publication for each Stonecutter node
        stonecutter.versions.forEach { node ->
            create<MavenPublication>(project.mod.name + "-" + node.project) {
                // Use the artifact from the node's project
                artifact(project.tasks.named("jar")) // or the appropriate jar task
                // Optionally set custom coordinates
                groupId = project.group.toString() + "." + project.mod.name
                artifactId = project.name
                version = node.version.toString()
            }
        }
    }
}
