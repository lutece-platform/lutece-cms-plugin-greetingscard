/*==============================================================*/
/* Table : greetings_card_template                              */
/*==============================================================*/
DROP TABLE IF EXISTS greetings_card_template;
CREATE TABLE greetings_card_template
(
    id_gct TINYINT NOT NULL,
	description VARCHAR(255),
	password VARCHAR(10),
	height int,
	width int,
	status TINYINT DEFAULT '0' NOT NULL,
	object_email VARCHAR(30),
	workgroup_key VARCHAR(50) DEFAULT NULL,
	PRIMARY KEY (id_gct)
);

/*==============================================================*/
/* Table : greetings_card                                          */
/*==============================================================*/
DROP TABLE IF EXISTS greetings_card;
CREATE TABLE greetings_card
(
	id_gc VARCHAR(32) NOT NULL,
	sender_name VARCHAR(80) DEFAULT '' NOT NULL,
	sender_email VARCHAR(80) NOT NULL,
	recipient_email VARCHAR(255) NOT NULL,
	message text NOT NULL,
	message2 text,
	date Date,
	sender_ip VARCHAR(20),
	id_gct TINYINT NOT NULL,
	status SMALLINT DEFAULT 0,
	is_copy SMALLINT DEFAULT 0,
	notify_user SMALLINT DEFAULT 0,
	domain_name VARCHAR(80),
	PRIMARY KEY (id_gc)
);

DROP TABLE IF EXISTS greetings_card_archive;
CREATE TABLE greetings_card_archive
(
	id_archive INT NOT NULL,
	id_gct TINYINT NOT NULL,
	domain_name VARCHAR(80) NOT NULL,
	nb_card INT NOT NULL,
	nb_card_red INT NOT NULL,
	year_archive INT NOT NULL,
	PRIMARY KEY (id_archive)
);