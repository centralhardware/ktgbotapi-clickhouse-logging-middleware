plugins {
    java
    `maven-publish`
    kotlin("jvm") version "2.0.21"
    id("com.ncorti.ktfmt.gradle") version "0.21.0"
}

group = "me.centralhardware.telegram"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.inmo:tgbotapi:20.0.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.clickhouse:clickhouse-jdbc:0.7.1")
    implementation("com.clickhouse:clickhouse-http-client:0.7.1-patch1")
    implementation("org.lz4:lz4-java:1.8.0")
    implementation("com.github.seratch:kotliquery:1.9.0")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "me.centralhardware"
            artifactId = "bot-common"
            version = "1.0-SNAPSHOT"
            from(components["java"])
        }
    }
}

ktfmt {
    kotlinLangStyle()
}