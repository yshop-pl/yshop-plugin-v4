plugins {
    `maven-publish`
}

publishing {
    repositories {
        maven {
            name = "yCodeRepository"
            url = uri("https://repository.ycode.pl/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "pl.yshop.plugin"
            artifactId = "api"
            version = "4.0.4"
            from(components["java"])
        }
    }
}
repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.squareup.okhttp3:okhttp:4.10.0")
}