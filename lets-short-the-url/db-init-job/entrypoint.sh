#!/bin/sh

echo "Waiting for PostgreSQL to be available..."

until pg_isready -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER"; do
  sleep 2
done

echo "PostgreSQL is up - running init script..."

psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f /init-db.sql

echo "DB Init Complete!"
