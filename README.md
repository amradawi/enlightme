## Overview
Contributors to this project believe the best way to learn something is not by reading it but by reacalling it from your memory. This idea serves as the foundation of this project by gamifying the process to the user in terms of questions.

## Running orientdb 
docker run -d --name orientdb -p 2424:2424 -p 2480:2480 -v /tmp/orientdb/databases/:/orientdb/databases -e ORIENTDB_ROOT_PASSWORD=rootpwd orientdb

## Running MongoDb
docker run --name mongodb -p 27017:27017 -d mongo

## Running the Web APP
sbt run

## On-going features
- ETL pipeline
- Security/Authentication checks
- Cloud integration
- SlackBot
- Proper MVC
- Front-end development
- auto-completion
- Indexing
- Personalization
- Browser plugin
- Summarization
- Question Generator
