plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "mds"
include("mds_engine")
include("mds_serialization")
include("mds_serialization_gson")
include("mds_sessions")
include("engine")
include("engine:threading")
include("engine:logging")
findProject(":engine:logging")?.name = "logging"
include("test")
include("engine:metrics")
findProject(":engine:metrics")?.name = "metrics"
include("engine:pipelines")
findProject(":engine:pipelines")?.name = "pipelines"
include("engine:routing")
findProject(":engine:routing")?.name = "routing"
