# TODOs for db-initializer

- [ ] Reduce Docker image size (currently ~355MB)
    - Investigate using `distroless` or multi-stage builds with `alpine`
    - Consider using `jlink` to create a minimal JVM
    - Strip unnecessary Spring dependencies (like Web, if not used)
    - Explore native builds with GraalV