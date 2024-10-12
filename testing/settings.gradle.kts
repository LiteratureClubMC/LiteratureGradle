pluginManagement {
    includeBuild("../plugin")
    repositories {
        mavenCentral()
        maven("https://nexus.velocitypowered.com/repository/maven-public/")
        maven("https://www.jitpack.io")
    }
}