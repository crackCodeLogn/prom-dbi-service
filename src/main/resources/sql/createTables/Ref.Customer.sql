CREATE TABLE IF NOT EXISTS public.cust
(
    id_cust integer NOT NULL,
    id_comp integer NOT NULL,
    name_first character varying(25) COLLATE pg_catalog."default" NOT NULL,
    name_last character varying(25) COLLATE pg_catalog."default",
    contact_cust character varying(55) COLLATE pg_catalog."default",
    CONSTRAINT cust_pkey PRIMARY KEY (id_cust)
);