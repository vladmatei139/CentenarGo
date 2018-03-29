ALTER TABLE userroutes ADD CONSTRAINT userroutes_currentlandmark_fk FOREIGN KEY (currentlandmark) REFERENCES landmarks(id);
ALTER TABLE userroutes ALTER currentlandmark SET NOT NULL;

ALTER TABLE userdetails ADD CONSTRAINT userdetails_currentroute_fk FOREIGN KEY (currentroute) REFERENCES routes(id);