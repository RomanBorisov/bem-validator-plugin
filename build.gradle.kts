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
version = "0.1.1"

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
    // Exclude test sources
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
        version.set("0.1.1")
        sinceBuild.set("231")
        untilBuild.set("251.*")
    }
    
    signPlugin {
        certificateChainFile.set(file("chain.crt"))
        privateKeyFile.set(file("private.pem"))
        password.set(providers.environmentVariable("PRIVATE_KEY_PASSWORD"))
    }
    
    publishPlugin {
        token.set(providers.environmentVariable("PUBLISH_TOKEN"))
        channels.set(listOf("default"))
    }
    
    named("buildSearchableOptions") {
        enabled = false
    }

    // Disable test-related tasks
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
