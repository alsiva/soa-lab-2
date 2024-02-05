plugins {
    war
}
repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":service"))

    compileOnly("jakarta.platform:jakarta.jakartaee-web-api:9.0.0")
    compileOnly("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
    compileOnly("jakarta.ws.rs:jakarta.ws.rs-api:3.0.0")
}

