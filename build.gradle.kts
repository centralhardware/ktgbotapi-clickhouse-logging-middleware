plugins {
    java
    `maven-publish`
    kotlin("jvm") version "2.0.21"
}

group = "me.centralhardware.telegram"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.inmo:tgbotapi:18.2.2")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.4")
    implementation("com.clickhouse:clickhouse-jdbc:0.7.0")
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