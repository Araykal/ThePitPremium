plugins {
    id("java")
    id("io.freefair.lombok") version "8.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("jvm")
}

val pluginName = "ThePitPremium"
val version = "3.93"
repositories {
    maven("https://maven.aliyun.com/repository/public/")
    mavenCentral()

    maven("https://repo.crazycrew.us/releases")
    maven("https://repo.codemc.io/repository/nms/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.inventivetalent.org/content/groups/public/")
    maven("https://jitpack.io")
    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.panda-lang.org/releases")
    mavenLocal()
}

dependencies {
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    compileOnly("org.mongodb:mongo-java-driver:3.12.11")
    compileOnly("org.mongojack:mongojack:4.8.1")
    compileOnly("org.mongodb:mongodb-driver-sync:5.2.0")
    compileOnly("redis.clients:jedis:2.9.0")
    compileOnly("org.java-websocket:Java-WebSocket:1.6.0")
    compileOnly("it.unimi.dsi:fastutil:8.5.13")
    api(libs.slf4j)
    api(libs.book)
    api(libs.slf4j)
    api(libs.reflectionhelper)
    compileOnly(libs.luckperms)
    compileOnly(libs.narshorn)
    compileOnly(libs.httpclient)
    compileOnly(libs.httpcore)
    compileOnly(libs.decentholograms)
    compileOnly(libs.protocollib)
    compileOnly(libs.papi)
    compileOnly(libs.playerpoints)
    compileOnly(libs.spigot.get8())
}

tasks.shadowJar {
    archiveFileName.set("$pluginName-$version.jar")
    mergeServiceFiles()
    exclude("META-INF/**")

}
tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}
kotlin {
    jvmToolchain(11)
}