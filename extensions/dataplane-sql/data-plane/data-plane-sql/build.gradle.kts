plugins {
    `java-library`
}

dependencies {
    
    implementation(libs.edc.spi.dataplane)
    implementation(libs.edc.spi.validator)
    implementation(libs.edc.spi.core)
    implementation(libs.edc.core.sql.lib)
    implementation(libs.edc.core.dataplane.util)
    implementation(libs.edc.lib.util)

    implementation(project(":extensions:dataplane-sql:common:sql-dataaddress"))

    testImplementation(libs.edc.junit)
    testImplementation(libs.jakarta.json.api)
    testImplementation(libs.jackson.datatype.jakarta.jsonp)
    testImplementation(libs.parsson)
    testImplementation(libs.restAssured)
    testImplementation(libs.awaitility)
    testImplementation(libs.edc.fc.core)
    testImplementation(libs.edc.lib.transform)
    testImplementation(libs.edc.lib.jsonld)
    testImplementation(libs.edc.controlplane.transform)
    testImplementation(libs.edc.lib.http)

    testImplementation(libs.restAssured)
    testImplementation(libs.mockserver.netty)
}


