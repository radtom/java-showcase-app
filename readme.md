# Java Showcase App
A simple Spring Boot 4/Java 25 app.
Handles Items, that can be tagged. Allows for creates, updates, and searching.

## How to run
Just run `docker compose up` in /docker folder.
This will spin up PostgreSQL Datbase, run Liquibase which will create the DB model and add test data.
Finally, the app image from ghcr repository will run.

The application can be accessed at localhost:8080/ - Swagger with interactive API 
definition runs here - you can experiment with API calls there.

## App configuration
There are following configuration properties:
* `item.expiration-seconds` - number of second since last update after which Item should be deleted - default: 600
* `item.delete-cron` - cron expression which specifies, how often deleting of the old items should run - default: 0 * * * * * (once per minute)
* `spring.cache.caffeine.spec` - configuration of caffeine cache - how long should cached result be kept and how many total cached results are allowed, default: expireAfterWrite=10m,maximumSize=500

These can also be passed as environment variables in format `ITEM_EXPIRATIONSECONDS`, `ITEM_DELETECRON`, `SPRING_CACHE_CAFFEINE_SPEC`.

## Technologies used
    * Java 25
    * Spring Boot 4
    * Spring Data JPA
    * PostgreSQL 17 DB
    * Liquibase - for creating DB model and adding data
    * caffeine in-memory cache 

## Reository structure
* /.github - contains github actions - build.yml CI for Building, testing ans static code analysis on merge and publish_image.yml for publishing app image to repository.
* /db - contains Liqibase scripts for Database model. Liquibase creates the tables and indexes, and also inserts test data
* /docker - contains docker-compose file for running the whole app environment
* /src - Java source code


## Notes on implementation
I decided to use the tag text value as a primary key for the tags table - this is very unusual, but sufficient for this simple app.
More standard approach would be to have numeric primary key.

App uses Optimistic Locking to handle concurrent updates using Hibernate @Version. If two users try to update the same Item, one of the will receive an exception
that the resource was updated in the meantime and should try again.

App uses caffeine in-memory cache for search results caching. There is TTL configured, so there are no explicit cache evicts on update or insert.

Searching by tags is case-sensitive and will return all Items that have at least one of the specified tags.

The old data are removed by automatic scheduled job, with configurable frequency

