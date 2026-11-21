import gg.meza.stonecraft.mod

plugins {
    id("gg.meza.stonecraft")
    id("com.hypherionmc.modutils.modpublisher") version "2.2.4"
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
        stonecutter.current.project.startsWith("26.1") -> 25
        stonecutter.current.project.startsWith("26.1.1") -> 25
        stonecutter.current.project.startsWith("26.1.2") -> 25
        stonecutter.current.project.startsWith("26.2") -> 25
        stonecutter.current.project.startsWith("26.3") -> 25
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
            api("teamreborn:energy:${project.mod.prop("energy")}")
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

val loader = project.name.substringAfterLast('-')
val mcVersion = project.name.substringBeforeLast('-')

tasks.named("publishMod") {
    dependsOn(if (tasks.names.contains("remapJar")) "remapJar" else "jar")
}

val dotenv: Map<String, String> = rootProject.file(".env")
    .takeIf { it.exists() }
    ?.readLines()
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() && !it.startsWith("#") && "=" in it }
    ?.associate {
        it.substringBefore("=").trim() to
                it.substringAfter("=").trim().removeSurrounding("\"")
    }
    ?: emptyMap()

fun secret(name: String): String? =
    providers.environmentVariable(name).orNull ?: dotenv[name]

publisher {
    apiKeys {
        modrinth(secret("MODRINTH_TOKEN"))
        curseforge(secret("CURSE_TOKEN"))
    }

    modrinthID.set("cQ18s4e8")
    curseID.set("1634651")


    versionType.set("release")
    projectVersion.set("${property("mod.version")}+$mcVersion-$loader")
    displayName.set("Starlib ${property("mod.version")} [$loader $mcVersion]")

    if (loader == "fabric") {
        modrinthDepends { required("fabric-api") }
        curseDepends { required("fabric-api") }
    }
    modrinthDepends {
        required("architectury-api")
    }
    curseDepends {
        required("architectury-api")
    }

    gameVersions.set(listOf(mcVersion))
    changelog.set(rootProject.file("CHANGELOG.md").takeIf { it.exists() }?.readText() ?: "No changelog provided.")
    loaders.set(listOf(loader))
    curseEnvironment.set("both")

    artifact.set(
        if (tasks.names.contains("remapJar")) tasks.named("remapJar") else tasks.named("jar")
    )
}
