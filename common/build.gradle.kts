plugins {
    id("java")
}

group = "ru.meproject.distributify"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("redis.clients:jedis:4.4.3")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(":api"))
    implementation("redis.clients:jedis:4.4.3")
}

tasks.test {
    useJUnitPlatform()
}