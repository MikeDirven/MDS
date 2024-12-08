plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "mds"
//include("mds_engine")
//include("mds_serialization")
//include("mds_serialization_gson")
//include("mds_sessions")
include("engine")
include("engine:threading")
include("engine:logging")
include("test")
include("engine:metrics")
include("engine:pipelines")
include("engine:routing")
include("utils")
include("plugins")
include("plugins:templating-kotlinx-html")
findProject(":plugins:templating-kotlinx-html")?.name = "templating-kotlinx-html"
