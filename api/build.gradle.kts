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
            version = "4.0.0"
            from(components["java"])
        }
    }
}

dependencies {

}