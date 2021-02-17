CREATE TABLE IF NOT EXISTS public.cpnt
(
    id_cpnt integer NOT NULL,
    name_cpnt character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT cpnt_pkey PRIMARY KEY (id_cpnt)
);