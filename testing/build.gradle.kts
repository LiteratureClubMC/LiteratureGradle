plugins {
    id("java")
    id("rip.sayori.lcmc.lg")
}

group = "rip.sayori"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

}
minecraft{
    version = "0.2.1-alpha"
}
dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}