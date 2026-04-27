#!/bin/sh
# Render gives connectionString as postgres://user:pass@host:port/db
# Spring Boot needs jdbc:postgresql://host:port/db
export DB_URL=$(echo "$DB_URL" | sed 's|postgres://|jdbc:postgresql://|')
exec java -jar app.jar --server.port=${PORT:-8081}