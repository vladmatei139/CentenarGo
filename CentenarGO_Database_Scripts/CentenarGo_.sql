CREATE TABLE answers (
    id integer NOT NULL,
    text character varying(300) NOT NULL,
    questionid integer NOT NULL,
    iscorrect boolean NOT NULL
);

CREATE SEQUENCE answers_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE images (
    id integer NOT NULL,
    userid integer NOT NULL,
    landmarkid integer NOT NULL,
    title character varying(60),
    path character varying(300) NOT NULL
);

CREATE SEQUENCE images_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE landmarks (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    content character varying(500) NOT NULL,
    route integer NOT NULL,
    latitude real NOT NULL,
    longitute real NOT NULL,
    routeorder integer NOT NULL
);

CREATE SEQUENCE landmarks_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE questions (
    id integer NOT NULL,
    text character varying(300) NOT NULL,
    landmarkid integer NOT NULL
);

CREATE SEQUENCE questions_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE routes (
    id integer NOT NULL,
    name character varying(30) NOT NULL
);

CREATE SEQUENCE routes_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE userdetails (
    userid integer NOT NULL,
    lastname character varying(40),
    firstname character varying(40),
    currentroute integer
);

CREATE TABLE userroutes (
    userid integer NOT NULL,
    routeid integer NOT NULL,
    datecompleted timestamp without time zone,
    currentlandmark integer
);

CREATE TABLE users (
    id integer NOT NULL,
    email character varying(100) NOT NULL,
    password character varying(40) NOT NULL,
    username character varying(40) NOT NULL
);

CREATE SEQUENCE users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

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
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


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
-- Name: userroutes userroutes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY userroutes
    ADD CONSTRAINT userroutes_pkey PRIMARY KEY (userid, routeid);


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
-- Name: images images_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY images
    ADD CONSTRAINT images_userid_fkey FOREIGN KEY (userid) REFERENCES users(id);


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
-- Name: userdetails userdetails_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY userdetails
    ADD CONSTRAINT userdetails_userid_fkey FOREIGN KEY (userid) REFERENCES users(id);


--
-- Name: userroutes userroutes_routeid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY userroutes
    ADD CONSTRAINT userroutes_routeid_fkey FOREIGN KEY (routeid) REFERENCES routes(id);


--
-- Name: userroutes userroutes_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY userroutes
    ADD CONSTRAINT userroutes_userid_fkey FOREIGN KEY (userid) REFERENCES users(id);

