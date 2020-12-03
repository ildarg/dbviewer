Project: DB Viewer (REST API)
Author: Ildar Ginatullin

Goal: implementation of API to maintain list of connection details and provide API to show DB structure and data view

Functionality:
- CRUD of connection details (get all, get one, post, put, delete)
For defined connection id
- Get the list of schema 
- Get the list of tables (schema is optional filter)
- Get the list of columns (schema and table are optional filters)
- Get the data for defined schema and table

How to run:
1. Install Postgresql (version 10+)
2. Create user in PostgreSQL (optional)
3. Create db istance in PostgreSQL
4. Set up application.properties file in root folder of the project to define DB connection details (spring.datasource.url, spring.datasource.username, spring.datasource.password)
5. Build and run application

Notice: to simplify testing DB is automatically clear db and recreate initial data when application run
initial db structure in schema-postgres.sql
initial db data in data-postgres.sql

Not implemented: 
- Comments for auto documentation
- Cotroller and Logger in AOP
- Meta data for each column
- Coverage for all services, only cotroller tests provided 
- Data return as String to simplify project

API provided:
GET /api/connections
GET /api/connections/:id
POST /api/connections
PUT /api/connections
DELETE /api/connections/:id

GET /api/connections/:id/schemas
GET /api/connections/:id/tables?schema=:schema                  # schema is optional
GET /api/connections/:id/columns?schema=:schema&table=:table    # schema, table are optional
GET /api/connections/:id/data?schema=:schema&table=:table       # schema, table are mandatory
