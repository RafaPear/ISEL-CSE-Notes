rootProject.name = "isip3"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

include(":cli")
include(":core")