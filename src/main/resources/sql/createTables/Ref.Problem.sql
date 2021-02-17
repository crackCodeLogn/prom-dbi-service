CREATE TABLE IF NOT EXISTS public.prob
(
    id_prob integer NOT NULL,
    name_prob character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT prob_pkey PRIMARY KEY (id_prob)
);
