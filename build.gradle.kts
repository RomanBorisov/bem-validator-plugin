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
version = "0.1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

sourceSets {
    main {
        kotlin.srcDirs("src/main/kotlin")
        resources.srcDirs("src/main/resources")
    }
    // Исключаем тестовые исходники
    test {
        kotlin.srcDirs()
        resources.srcDirs()
    }
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from("src/main/resources") {
            include("inspectionDescriptions/**")
            include("META-INF/**")
        }
    }

    patchPluginXml {
        version.set("0.1.0")
        sinceBuild.set("231")
        untilBuild.set("241.*")
    }
    
    named("buildSearchableOptions") {
        enabled = false
    }

    // Отключаем задачи, связанные с тестами
    named("test") {
        enabled = false
    }
    
    named("compileTestKotlin") {
        enabled = false
    }
    
    named("compileTestJava") {
        enabled = false
    }
    
    named("processTestResources") {
        enabled = false
    }
} 
