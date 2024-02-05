plugins {
    war
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("jakarta.platform:jakarta.jakartaee-web-api:9.0.0")
    compileOnly("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
    compileOnly("jakarta.persistence:jakarta.persistence-api:3.0.0")
    compileOnly("jakarta.ws.rs:jakarta.ws.rs-api:3.0.0")
    compileOnly("jakarta.security.enterprise:jakarta.security.enterprise-api:2.0.0")
    compileOnly("jakarta.servlet:jakarta.servlet-api:5.0.0")
    compileOnly("jakarta.inject:jakarta.inject-api:2.0.1")
    implementation("io.swagger.core.v3:swagger-jaxrs2:2.2.18")
    implementation("org.hsqldb:hsqldb:2.7.2")
}
