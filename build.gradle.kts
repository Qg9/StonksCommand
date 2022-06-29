plugins {
    kotlin("jvm") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    `maven-publish`
}

group = "fr.qg"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url="https://maven.imanity.dev/repository/imanity-libraries")
}

dependencies {
    compileOnly("org.imanity.paperspigot:paper1.8.8:1.8.8")
}
