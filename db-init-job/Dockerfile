# Use a minimal Alpine base image
FROM alpine:3.18

# Install only the PostgreSQL client and bash
RUN apk add --no-cache postgresql-client bash

# Copy SQL and entrypoint
COPY init-db.sql /init-db.sql
COPY entrypoint.sh /entrypoint.sh

# Make sure the entrypoint script is executable
RUN chmod +x /entrypoint.sh

# Set default entrypoint
ENTRYPOINT ["/entrypoint.sh"]