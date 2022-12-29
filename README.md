# Steps to reproduce

- import project into IDEA (from existing sources)
- compile using maven (this generates sources via `kapt`)
- (IDEA still doesn't now about generated sources, i.e. `com.example.blazedemo.views.AnimalViewBuilder` import is "red" in the IDE)
- reload maven project (from the Maven toolbar)
- (Now because there is `<sourceDir>target/generated-sources/kapt</sourceDir>` in the pom the directory is marked as "sources root" and the import is no longer red.)
- "Build Project" using the "hammer" -> fails on non-existent `AnimalViewBuilder` even though the source are correctly marked
- unmark `target/generated-sources/kapt` and mark it again as "sources root" (or "generated sources root")
- "Build Project" again -> fails on "java: package com.blazebit.persistence does not exist" when compiling one of the generated sources
- Now there appears to be no way how to get rid of the error.
  - Also, it's strange that manually unmarking and marking the sources the directory helped get rid off the first error.

# Notes

- When I created the project from scratch (via Spring Initializr) then everything worked correctly, i.e. I was able to manually run `kapt` and then build project in IDEA, even run the Spring app.
- After some action, however, I started to get the "package does not exist" errors and I couldn't get rid off them. I even tried clean reimport from scratch (close project -> delete IDEA files `.idea` and `blaze-demo.iml` -> restart IDEA -> import project according to steps above) but still kept getting the same error.
- Not sure exactly what the breaking action was but I suspect "Rebuild Project".
