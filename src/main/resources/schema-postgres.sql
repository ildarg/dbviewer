DROP TABLE IF EXISTS public.connections;
DROP SEQUENCE IF EXISTS public.connections_details_seq;
CREATE TABLE public.connections (
  id BIGINT NOT NULL,
  name VARCHAR(255),
  dbname VARCHAR(255),
  hostname VARCHAR(255),
  port INTEGER,
  username VARCHAR(255),
  password VARCHAR(255),
  created_date date DEFAULT now(),
  CONSTRAINT connections_pkey PRIMARY KEY(id),
  CONSTRAINT connections_ukey UNIQUE (name)
);

CREATE SEQUENCE public.connections_details_seq START 4;