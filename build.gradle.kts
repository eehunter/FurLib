plugins {
    id("fabric-loom")
    //val kotlinVersion: String by System.getProperties()
    kotlin("jvm").version("1.6.10")
    `maven-publish`
    java
}
base {
    val archivesBaseName: String by project
    archivesName.set(archivesBaseName)
}
val modVersion: String by project
version = modVersion
val mavenGroup: String by project
group = mavenGroup
//minecraft {}
repositories {
    maven { setUrl("https://ladysnake.jfrog.io/artifactory/mods");name = "Ladysnake Libs" }
    maven { setUrl("https://maven.cafeteria.dev");content { includeGroup("net.adriantodt.fabricmc") } }
    maven { setUrl("https://maven.jamieswhiteshirt.com/libs-release");content { includeGroup ("com.jamieswhiteshirt") } }
    maven { setUrl("https://jitpack.io") }
    maven { setUrl("https://maven.shedaniel.me/") }
    maven { setUrl("https://maven.terraformersmc.com/") }
    maven { setUrl("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/") }
}
dependencies {
    fun ExternalModuleDependency.excludeFabricApi(){
        exclude("net.fabricmc")
    }
    val minecraftVersion: String by project
    minecraft("com.mojang:minecraft:$minecraftVersion")
    val yarnMappings: String by project
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    val loaderVersion: String by project
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    val fabricVersion: String by project
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    val fabricKotlinVersion: String by project
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")
    val apoliVersion: String by project
    modImplementation("com.github.apace100:apoli:${apoliVersion}"){excludeFabricApi()}
    include("com.github.apace100:apoli:${apoliVersion}"){excludeFabricApi()}
    val originsVersion: String by project
    //modRuntimeOnly("com.github.apace100:origins-fabric:${originsVersion}"){excludeFabricApi()}
    val geckoLibVersion: String by project
    modImplementation("software.bernie.geckolib:geckolib-fabric-1.18:${geckoLibVersion}"){excludeFabricApi()}
}
tasks {
    val javaVersion = JavaVersion.VERSION_17
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions { jvmTarget = javaVersion.toString() }
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
    }
    jar { from("LICENSE") { rename { "${it}_${base.archivesName}" } } }
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") { expand(mutableMapOf("version" to project.version)) }
    }
    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(javaVersion.toString())) }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        withSourcesJar()
    }
}