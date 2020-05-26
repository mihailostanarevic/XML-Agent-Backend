create table IF NOT EXISTS system_message (id integer not null, content varchar(255), primary key (id));

CREATE TABLE IF NOT EXISTS acl_sid (
  id integer NOT NULL,
  principal integer NOT NULL UNIQUE,
  sid varchar(100) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS acl_class (
  id integer NOT NULL,
  class varchar(255) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS acl_entry (
  id integer NOT NULL,
  acl_object_identity integer NOT NULL  UNIQUE,
  ace_order integer NOT NULL  UNIQUE,
  sid integer NOT NULL,
  mask integer NOT NULL,
  granting integer NOT NULL,
  audit_success integer NOT NULL,
  audit_failure integer NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS acl_object_identity (
  id integer NOT NULL,
  object_id_class integer NOT NULL UNIQUE ,
  object_id_identity integer NOT NULL UNIQUE ,
  parent_object integer DEFAULT NULL,
  owner_sid integer DEFAULT NULL,
  entries_inheriting integer NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE acl_entry
ADD FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);

ALTER TABLE acl_entry
ADD FOREIGN KEY (sid) REFERENCES acl_sid(id);

--
-- Constraints for table acl_object_identity
--
ALTER TABLE acl_object_identity
ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (object_id_class) REFERENCES acl_class (id);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid (id);