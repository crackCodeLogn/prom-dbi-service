CREATE TABLE IF NOT EXISTS public.comp
(
    id_comp integer NOT NULL,
    name_comp character varying(30) COLLATE pg_catalog."default" NOT NULL,
    contact_comp character varying(55) COLLATE pg_catalog."default",
    person_contact_comp character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT comp_pkey PRIMARY KEY (id_comp)
);