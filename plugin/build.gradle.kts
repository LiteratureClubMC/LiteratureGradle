plugins {
    id("java")
    id("java-gradle-plugin")
    kotlin("jvm") version "2.0.20"
}

group = "rip.sayori"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.minecraftforge.net/")
    maven("https://www.jitpack.io")

    //maven("https://maven.services.pocolifo.com/releases")
}

dependencies {
    implementation("commons-io:commons-io:2.8.0")
    //implementation("net.minecraftforge:ForgeAutoRenamingTool:0.1.24")
    implementation("com.github.NotMonika-FuckThe2fa:Vignette:1.0")
    implementation("net.minecraftforge:accesstransformers:8.0.4")
    implementation("com.alibaba:fastjson:2.0.53")
    implementation("net.minecraftforge:mergetool:1.1.5")
    //implementation("com.pocolifo:jarremapper:2.0.2")
    implementation("de.siegmar:fastcsv:3.3.1")
    implementation("com.github.LiteratureClubMC:BinaryPatcher:1.5")

}
gradlePlugin{
    plugins{
        create("DependenciesPlugin"){
            id = "rip.sayori.lcmc.lg"
            implementationClass = "rip.sayori.lcmc.lg.LiteratureGradle"
        }
    }
}