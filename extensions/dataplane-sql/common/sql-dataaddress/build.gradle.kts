plugins {
    `java-library`
}

dependencies {
    //implementation(project("org.eclipse.edc.connector.sql.dataaddress"))
    implementation(libs.edc.spi.validator)
    implementation(libs.edc.spi.transfer)
    implementation(libs.edc.spi.core)
    implementation(libs.edc.lib.util)

}