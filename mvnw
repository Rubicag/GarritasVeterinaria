#!/usr/bin/env sh
# Wrapper for Maven (POSIX)
DIRNAME=$(cd "$(dirname "$0")" && pwd)
# Remove trailing slash if present
DIRNAME=${DIRNAME%/}
# Ensure the multi-module project directory is set for Maven
java -Dmaven.multiModuleProjectDirectory="$DIRNAME" -cp "$DIRNAME/.mvn/wrapper/maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain "$@"
