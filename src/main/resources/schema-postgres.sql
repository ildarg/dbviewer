DROP TABLE IF EXISTS public.connections;
CREATE TABLE public.connections (
  id BIGINT NOT NULL,
  name VARCHAR(255),
  dbname VARCHAR(255),
  hostname VARCHAR(255),
  port INTEGER,
  username VARCHAR(255),
  password VARCHAR(255),
  CONSTRAINT connections_pkey PRIMARY KEY(id),
  CONSTRAINT connections_ukey UNIQUE (name)
)