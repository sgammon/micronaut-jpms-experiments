
plugins {
    id("build.less") version("1.0.0-rc2")
}

buildless {
    // This block is optional; use it to further configure your build cache. Make sure to
    // install and run the Buildless Agent (https://github.com/buildless/cli) for best results.
    //
    // Remember to set BUILDLESS_APIKEY in your environment if you are using Buildless Cloud.
    // The agent and plugin will pick up your API key automatically.
}


rootProject.name="demo"

include(":libmodule")
include(":appmodule")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
