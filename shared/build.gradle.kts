plugins {
    id("java")
}

group = "pl.yshop.plugin.shared"
version = "4.0.0"

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases")
}

dependencies {
    api(project(":api"))
    api("com.squareup.okhttp3:okhttp:4.10.0")
    compileOnly("com.google.code.gson:gson:2.8.6")
}
