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
   

    
}


