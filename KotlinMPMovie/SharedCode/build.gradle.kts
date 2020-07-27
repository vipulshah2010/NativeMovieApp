import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.3.70"
}

kotlin {
    //select iOS target platform depending on the Xcode environment variables
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iOSTarget("ios") {
        binaries {
            framework {
                baseName = "SharedCode"
            }
        }
    }

    jvm("android")

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        implementation(kotlin("stdlib-common"))
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.20.0-1.4-M1-release-99")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.7-mpp-dev-11")
        implementation("io.ktor:ktor-client-core:1.3.2-1.4-M3")
        implementation("io.ktor:ktor-client-serialization:1.3.2-1.4-M3")
    }

    sourceSets["androidMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.7-1.4-M3")
        implementation("io.ktor:ktor-client-android:1.3.2-1.4-M3")
        implementation("io.ktor:ktor-client-serialization-jvm:1.3.2-1.4-M3")
    }

    sourceSets["iosMain"].dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.20.0-1.4-M1-release-99")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.3.7-mpp-dev-11")
        implementation("io.ktor:ktor-client-ios:1.3.2-1.4-M3")
        implementation("io.ktor:ktor-client-serialization-native:1.3.2-1.4-M1-2")
    }
}


val packForXcode by tasks.creating(Sync::class) {
    group = "build"

    //selecting the right configuration for the iOS framework depending on the Xcode environment variables
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>("ios").binaries.getFramework(mode)

    inputs.property("mode", mode)
    dependsOn(framework.linkTask)

    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)

    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText("#!/bin/bash\nexport 'JAVA_HOME=${System.getProperty("java.home")}'\ncd '${rootProject.rootDir}'\n./gradlew \$@\n")
        gradlew.setExecutable(true)
    }
}

tasks.getByName("build").dependsOn(packForXcode)