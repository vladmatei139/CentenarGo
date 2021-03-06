DELETE FROM Routes;
DELETE FROM Landmarks;
DELETE FROM Questions;
DELETE FROM Answers;

ALTER SEQUENCE routes_id_seq RESTART WITH 1;
ALTER SEQUENCE landmarks_id_seq RESTART WITH 1;
ALTER SEQUENCE questions_id_seq RESTART WITH 1;
ALTER SEQUENCE answers_id_seq RESTART WITH 1;

INSERT INTO Routes (name) VALUES ('Traseu scurt');
INSERT INTO Routes (name) VALUES ('Traseu mediu');
INSERT INTO Routes (name) VALUES ('Traseu lung');

INSERT INTO Landmarks (name, content, route, latitude, longitude, routeorder) VALUES ('Obiectiv 11', 'Lorem ipsum 11', 1, 44.430406, 26.088898, 1);
INSERT INTO Landmarks (name, content, route, latitude, longitude, routeorder) VALUES ('Obiectiv 12', 'Lorem ipsum 12', 1, 44.431902, 26.095889, 2);
INSERT INTO Landmarks (name, content, route, latitude, longitude, routeorder) VALUES ('Obiectiv 13', 'Lorem ipsum 13', 1, 44.435182, 26.100483, 3);
INSERT INTO Landmarks (name, content, route, latitude, longitude, routeorder) VALUES ('Obiectiv 21', 'Lorem ipsum 21', 2, 44.467015, 26.078019, 1);
INSERT INTO Landmarks (name, content, route, latitude, longitude, routeorder) VALUES ('Obiectiv 22', 'Lorem ipsum 22', 2, 44.473815, 26.089975, 2);
INSERT INTO Landmarks (name, content, route, latitude, longitude, routeorder) VALUES ('Obiectiv 23', 'Lorem ipsum 23', 2, 44.480138, 26.102417, 3);
INSERT INTO Landmarks (name, content, route, latitude, longitude, routeorder) VALUES ('Obiectiv 24', 'Lorem ipsum 24', 2, 44.465889, 26.101992, 4);
INSERT INTO Landmarks (name, content, route, latitude, longitude, routeorder) VALUES ('Obiectiv 25', 'Lorem ipsum 25', 2, 44.457096, 26.100596, 5);
INSERT INTO Landmarks (name, content, route, latitude, longitude, routeorder) VALUES ('Obiectiv 31', 'Lorem ipsum 31', 3, 44.420442, 26.148183, 1);
INSERT INTO Landmarks (name, content, route, latitude, longitude, routeorder) VALUES ('Obiectiv 32', 'Lorem ipsum 32', 3, 44.424214, 26.152751, 2);
INSERT INTO Landmarks (name, content, route, latitude, longitude, routeorder) VALUES ('Obiectiv 33', 'Lorem ipsum 33', 3, 44.428643, 26.155532, 3);
INSERT INTO Landmarks (name, content, route, latitude, longitude, routeorder) VALUES ('Obiectiv 34', 'Lorem ipsum 34', 3, 44.431923, 26.153925, 4);
INSERT INTO Landmarks (name, content, route, latitude, longitude, routeorder) VALUES ('Obiectiv 35', 'Lorem ipsum 35', 3, 44.345695, 26.152904, 5);
INSERT INTO Landmarks (name, content, route, latitude, longitude, routeorder) VALUES ('Obiectiv 36', 'Lorem ipsum 36', 3, 44.439630, 26.150735, 6);
INSERT INTO Landmarks (name, content, route, latitude, longitude, routeorder) VALUES ('Obiectiv 37', 'Lorem ipsum 37', 3, 44.443164, 26.153261, 7);

INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 1', 1);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 2', 1);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 3', 1);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 1', 2);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 2', 2);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 3', 2);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 1', 3);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 2', 3);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 3', 3);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 1', 4);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 2', 4);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 3', 4);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 1', 5);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 2', 5);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 3', 5);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 1', 6);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 2', 6);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 3', 6);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 1', 7);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 2', 7);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 3', 7);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 1', 8);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 2', 8);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 3', 8);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 1', 9);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 2', 9);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 3', 9);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 1', 10);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 2', 10);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 3', 10);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 1', 11);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 2', 11);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 3', 11);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 1', 12);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 2', 12);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 3', 12);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 1', 13);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 2', 13);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 3', 13);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 1', 14);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 2', 14);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 3', 14);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 1', 15);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 2', 15);
INSERT INTO Questions (text, landmarkid) VALUES ('Intrebarea 3', 15);

INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 1, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 1, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 1, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 2, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 2, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 2, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 3, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 3, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 3, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 4, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 4, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 4, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 5, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 5, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 5, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 6, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 6, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 6, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 7, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 7, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 7, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 8, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 8, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 8, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 9, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 9, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 9, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 10, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 10, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 10, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 11, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 11, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 11, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 12, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 12, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 12, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 13, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 13, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 13, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 14, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 14, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 14, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 15, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 15, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 15, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 16, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 16, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 16, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 17, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 17, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 17, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 18, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 18, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 18, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 19, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 19, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 19, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 20, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 20, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 20, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 21, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 21, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 21, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 22, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 22, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 22, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 23, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 23, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 23, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 24, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 24, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 24, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 25, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 25, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 25, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 26, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 26, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 26, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 27, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 27, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 27, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 28, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 28, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 28, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 29, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 29, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 29, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 30, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 30, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 30, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 31, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 31, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 31, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 32, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 32, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 32, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 33, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 33, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 33, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 34, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 34, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 34, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 35, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 35, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 35, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 36, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 36, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 36, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 37, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 37, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 37, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 38, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 38, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 38, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 39, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 39, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 39, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 40, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 40, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 40, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 41, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 41, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 41, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 42, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 42, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 42, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 43, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 43, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 43, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 44, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 44, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 44, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 1', 45, FALSE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 2', 45, TRUE);
INSERT INTO Answers (text, questionid, iscorrect) VALUES ('Raspuns 3', 45, FALSE);
