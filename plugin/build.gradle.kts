plugins {
    id("java")
    id("java-gradle-plugin")
    kotlin("jvm") version "2.0.20"
}

group = "rip.sayori"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://nexus.velocitypowered.com/repository/maven-public/")
}

dependencies {
    implementation("commons-io:commons-io:2.8.0")
    implementation("net.minecraftforge:binarypatcher:1.1.1")
    implementation("net.minecraftforge:ForgeAutoRenamingTool:0.1.24")
    implementation("net.minecraftforge:accesstransformers:8.0.4")
    implementation("com.alibaba:fastjson:2.0.53")



}
gradlePlugin{
    plugins{
        create("DependenciesPlugin"){
            id = "rip.sayori.lcmc.lg"
            implementationClass = "rip.sayori.lcmc.lg.LiteratureGradle"
        }
    }
}