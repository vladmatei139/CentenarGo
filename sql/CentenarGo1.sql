--
-- PostgreSQL database dump
--

-- Dumped from database version 10.1
-- Dumped by pg_dump version 10.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: answers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE answers (
    id integer NOT NULL,
    text character varying(300) NOT NULL,
    questionid integer NOT NULL,
    iscorrect boolean NOT NULL
);


ALTER TABLE answers OWNER TO postgres;

--
-- Name: answers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE answers_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE answers_id_seq OWNER TO postgres;

--
-- Name: answers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE answers_id_seq OWNED BY answers.id;


--
-- Name: images; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE images (
    id integer NOT NULL,
    landmarkid integer NOT NULL,
    title character varying(60),
    path character varying(300) NOT NULL,
    userid uuid NOT NULL
);


ALTER TABLE images OWNER TO postgres;

--
-- Name: images_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE images_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE images_id_seq OWNER TO postgres;

--
-- Name: images_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE images_id_seq OWNED BY images.id;


--
-- Name: landmarks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE landmarks (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    content character varying(500) NOT NULL,
    route integer NOT NULL,
    latitude real NOT NULL,
    longitute real NOT NULL,
    routeorder integer NOT NULL
);


ALTER TABLE landmarks OWNER TO postgres;

--
-- Name: landmarks_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE landmarks_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE landmarks_id_seq OWNER TO postgres;

--
-- Name: landmarks_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE landmarks_id_seq OWNED BY landmarks.id;


--
-- Name: questions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE questions (
    id integer NOT NULL,
    text character varying(300) NOT NULL,
    landmarkid integer NOT NULL
);


ALTER TABLE questions OWNER TO postgres;

--
-- Name: questions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE questions_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE questions_id_seq OWNER TO postgres;

--
-- Name: questions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE questions_id_seq OWNED BY questions.id;


--
-- Name: routes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE routes (
    id integer NOT NULL,
    name character varying(30) NOT NULL
);


ALTER TABLE routes OWNER TO postgres;

--
-- Name: routes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE routes_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE routes_id_seq OWNER TO postgres;

--
-- Name: routes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE routes_id_seq OWNED BY routes.id;


--
-- Name: userdetails; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE userdetails (
    lastname character varying(40),
    firstname character varying(40),
    currentroute integer,
    userid uuid NOT NULL
);


ALTER TABLE userdetails OWNER TO postgres;

--
-- Name: userroutes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE userroutes (
    routeid integer NOT NULL,
    datecompleted timestamp without time zone,
    currentlandmark integer,
    userid uuid NOT NULL
);


ALTER TABLE userroutes OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE users (
    email character varying(100) NOT NULL,
    password character varying(255) NOT NULL,
    username character varying(40) NOT NULL,
    id uuid DEFAULT uuid_generate_v4() NOT NULL
);


ALTER TABLE users OWNER TO postgres;

--
-- Name: answers id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY answers ALTER COLUMN id SET DEFAULT nextval('answers_id_seq'::regclass);


--
-- Name: images id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY images ALTER COLUMN id SET DEFAULT nextval('images_id_seq'::regclass);


--
-- Name: landmarks id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY landmarks ALTER COLUMN id SET DEFAULT nextval('landmarks_id_seq'::regclass);


--
-- Name: questions id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY questions ALTER COLUMN id SET DEFAULT nextval('questions_id_seq'::regclass);


--
-- Name: routes id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY routes ALTER COLUMN id SET DEFAULT nextval('routes_id_seq'::regclass);


--
-- Data for Name: answers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY answers (id, text, questionid, iscorrect) FROM stdin;
\.


--
-- Data for Name: images; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY images (id, landmarkid, title, path, userid) FROM stdin;
\.


--
-- Data for Name: landmarks; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY landmarks (id, name, content, route, latitude, longitute, routeorder) FROM stdin;
\.


--
-- Data for Name: questions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY questions (id, text, landmarkid) FROM stdin;
\.


--
-- Data for Name: routes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY routes (id, name) FROM stdin;
\.


--
-- Data for Name: userdetails; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY userdetails (lastname, firstname, currentroute, userid) FROM stdin;
\.


--
-- Data for Name: userroutes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY userroutes (routeid, datecompleted, currentlandmark, userid) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY users (email, password, username, id) FROM stdin;
\.


--
-- Name: answers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('answers_id_seq', 1, false);


--
-- Name: images_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('images_id_seq', 1, false);


--
-- Name: landmarks_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('landmarks_id_seq', 1, false);


--
-- Name: questions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('questions_id_seq', 1, false);


--
-- Name: routes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('routes_id_seq', 1, false);


--
-- Name: answers answers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY answers
    ADD CONSTRAINT answers_pkey PRIMARY KEY (id);


--
-- Name: images images_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY images
    ADD CONSTRAINT images_pkey PRIMARY KEY (id);


--
-- Name: landmarks landmarks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY landmarks
    ADD CONSTRAINT landmarks_pkey PRIMARY KEY (id);


--
-- Name: questions questions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY questions
    ADD CONSTRAINT questions_pkey PRIMARY KEY (id);


--
-- Name: routes routes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY routes
    ADD CONSTRAINT routes_pkey PRIMARY KEY (id);


--
-- Name: userdetails userdetails_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY userdetails
    ADD CONSTRAINT userdetails_pkey PRIMARY KEY (userid);


--
-- Name: userroutes userid_routeid_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY userroutes
    ADD CONSTRAINT userid_routeid_pk PRIMARY KEY (userid, routeid);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: answers answers_questionid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY answers
    ADD CONSTRAINT answers_questionid_fkey FOREIGN KEY (questionid) REFERENCES questions(id);


--
-- Name: images images_landmarkid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY images
    ADD CONSTRAINT images_landmarkid_fkey FOREIGN KEY (landmarkid) REFERENCES landmarks(id);


--
-- Name: images images_userid_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY images
    ADD CONSTRAINT images_userid_fk FOREIGN KEY (userid) REFERENCES users(id);


--
-- Name: landmarks landmarks_route_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY landmarks
    ADD CONSTRAINT landmarks_route_fkey FOREIGN KEY (route) REFERENCES routes(id);


--
-- Name: questions questions_landmarkid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY questions
    ADD CONSTRAINT questions_landmarkid_fkey FOREIGN KEY (landmarkid) REFERENCES landmarks(id);


--
-- Name: userdetails userdetails_userid_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY userdetails
    ADD CONSTRAINT userdetails_userid_fk FOREIGN KEY (userid) REFERENCES users(id);


--
-- Name: userroutes userroutes_routeid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY userroutes
    ADD CONSTRAINT userroutes_routeid_fkey FOREIGN KEY (routeid) REFERENCES routes(id);


--
-- Name: userroutes userroutes_userid_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY userroutes
    ADD CONSTRAINT userroutes_userid_fk FOREIGN KEY (userid) REFERENCES users(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

GRANT USAGE ON SCHEMA public TO postgres;


--
-- PostgreSQL database dump complete
--

