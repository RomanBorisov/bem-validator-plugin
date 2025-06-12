plugins {
    id("org.jetbrains.intellij") version "1.17.3"
    kotlin("jvm") version "2.0.0"
}

intellij {
    version.set("2025.1")
    type.set("PS")
    plugins.set(listOf("com.jetbrains.php"))
}

group = "ru.bem"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // testImplementation("org.jetbrains.kotlin:kotlin-test") // Можно добавить, если нужны тесты
}

kotlin {
    jvmToolchain(17)
}

tasks {
    patchPluginXml {
        sinceBuild.set("251")
        untilBuild.set("259.*")
    }
    named("buildSearchableOptions") {
        enabled = false
    }
} 
