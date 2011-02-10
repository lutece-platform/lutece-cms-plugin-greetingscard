/*==============================================================*/
/* Table : greetings_card_template                              */
/*==============================================================*/
drop table if exists greetings_card_template;
create table greetings_card_template
(
    id_gct TINYINT NOT NULL,
	description VARCHAR(255),
	password VARCHAR(10),
	height int,
	width int,
	status TINYINT DEFAULT '0' NOT NULL,
	object_email VARCHAR(30),
	workgroup_key varchar(50) default NULL,
	PRIMARY KEY (id_gct)
);

/*==============================================================*/
/* Table : greetings_card                                          */
/*==============================================================*/
drop table if exists greetings_card;
create table greetings_card
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
	is_read SMALLINT DEFAULT 0,
	is_copy SMALLINT DEFAULT 0,
	PRIMARY KEY (id_gc)
);