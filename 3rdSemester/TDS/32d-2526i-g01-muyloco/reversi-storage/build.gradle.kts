dependencies {
    implementation(libs.okio)
    testImplementation(kotlin("test"))
    implementation(project(":reversi-utils"))
    implementation(libs.coroutines)
    implementation(platform(libs.mongoDB))
    implementation("org.mongodb:mongodb-driver-kotlin-sync")
    implementation("org.mongodb:bson-kotlinx")
}