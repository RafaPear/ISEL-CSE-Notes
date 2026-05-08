dependencies {
    implementation(project(":reversi-storage"))
    implementation(project(":reversi-utils"))
    testImplementation(kotlin("test"))
    testImplementation(libs.coroutines.test)
    implementation(libs.coroutines)
}