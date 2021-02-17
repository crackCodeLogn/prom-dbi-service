CREATE TABLE IF NOT EXISTS public.make
(
    id_make integer NOT NULL,
    name_make character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT make_pkey PRIMARY KEY (id_make)
);