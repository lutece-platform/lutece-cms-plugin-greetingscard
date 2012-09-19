
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url) VALUES ("GREETINGSCARD_MANAGEMENT","greetingscard.adminFeature.greetingscard_management.name",3,"jsp/admin/plugins/greetingscard/ManageGreetingsCard.jsp","greetingscard.adminFeature.greetingscard_management.description",0,"greetingscard","APPLICATIONS","images/admin/skin/plugins/greetingscard/greetingscard.png", NULL);
INSERT INTO core_user_right VALUES ('GREETINGSCARD_MANAGEMENT', 1);

INSERT INTO core_physical_file VALUES (123,'<?xml version=\"1.0\"?>\r\n<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\r\n	<xsl:output method=\"text\"/>\r\n	<xsl:template match=\"GreetingsCards\">\r\n		<xsl:text>\"sender name\";</xsl:text>\r\n		<xsl:text>\"sender mail\";</xsl:text>\r\n		<xsl:text>\"date\";</xsl:text>\r\n		<xsl:text>\"recipient email\";</xsl:text>\r\n		<xsl:text>\"greetingscard template id\";</xsl:text>\r\n		<xsl:text>\"status\";</xsl:text>\r\n		<xsl:text>\"is copy\"</xsl:text>\r\n		<xsl:text>&#10;</xsl:text>\r\n		<xsl:apply-templates select=\"greetingscard-data\" />\r\n	</xsl:template>\r\n	\r\n	<xsl:template match=\"greetingscard-data\">\r\n		<xsl:text>\"</xsl:text>\r\n		<xsl:value-of select=\"sender-name\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"sender-mail\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"date\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"recipient-email\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"greetingscard-template-id\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"status\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"is-copy\" />\r\n		<xsl:text>\"</xsl:text>\r\n		<xsl:text>&#10;</xsl:text>\r\n	</xsl:template>\r\n</xsl:stylesheet>\r\n');
INSERT INTO core_physical_file VALUES (124,'<?xml version=\"1.0\"?>\r\n<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\r\n	<xsl:output method=\"text\"/>\r\n	<xsl:template match=\"GreetingsCards\">\r\n		<xsl:text>&lt;?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?&gt;&#10;</xsl:text>\r\n		<xsl:text>&lt;GreetingsCards&gt;&#10;</xsl:text>\r\n		<xsl:apply-templates select=\"greetingscard-data\" />\r\n		<xsl:text>&lt;/GreetingsCards&gt;</xsl:text>\r\n	</xsl:template>\r\n	\r\n	<xsl:template match=\"greetingscard-data\">\r\n		<xsl:text>&lt;greetingscard-data&gt;&#10;</xsl:text>\r\n			<xsl:text>&lt;sender-name&gt;</xsl:text>\r\n				<xsl:value-of select=\"sender-name\" />\r\n			<xsl:text>&lt;/sender-name&gt;&#10;</xsl:text>\r\n			<xsl:text>&lt;sender-mail&gt;</xsl:text>\r\n				<xsl:value-of select=\"sender-mail\" />\r\n			<xsl:text>&lt;/sender-mail&gt;&#10;</xsl:text>\r\n			<xsl:text>&lt;date&gt;</xsl:text>\r\n				<xsl:value-of select=\"date\" />\r\n			<xsl:text>&lt;/date&gt;&#10;</xsl:text>\r\n			<xsl:text>&lt;recipient-email&gt;</xsl:text>\r\n				<xsl:value-of select=\"recipient-email\" />\r\n			<xsl:text>&lt;/recipient-email&gt;&#10;</xsl:text>\r\n			<xsl:text>&lt;greetingscard-template-id&gt;</xsl:text>\r\n				<xsl:value-of select=\"greetingscard-template-id\" />\r\n			<xsl:text>&lt;/greetingscard-template-id&gt;&#10;</xsl:text>\r\n			<xsl:text>&lt;status&gt;</xsl:text>\r\n				<xsl:value-of select=\"status\" />\r\n			<xsl:text>&lt;/status&gt;&#10;</xsl:text>\r\n			<xsl:text>&lt;is-copy&gt;</xsl:text>\r\n				<xsl:value-of select=\"is-copy\" />\r\n			<xsl:text>&lt;/is-copy&gt;&#10;</xsl:text>\r\n		<xsl:text>&lt;/greetingscard-data&gt;&#10;</xsl:text>\r\n	</xsl:template>\r\n</xsl:stylesheet>\r\n');

INSERT INTO core_file VALUES (123,'export_greetingscard_csv.xsl',123,1176,'application/xml');
INSERT INTO core_file VALUES (124,'export_greetingscard_xml.xsl',124,1606,'application/xml');

INSERT INTO core_xsl_export VALUES (123,'Greetingscard - Export CSV','Greetingscard - Export CSV','csv',123);
INSERT INTO core_xsl_export VALUES (124,'Greetingscard - Export XML','Greetingscard - Export XML','xml',124);

INSERT INTO core_template VALUES ('greetingscard_card_red_mail','Bonjour,<br />La carte que vous avez envoyé à ${recipient_email} vient d''être lue.');

INSERT INTO core_datastore VALUES ('greetingscard.nextAutoArchiving', '');
INSERT INTO core_datastore VALUES ('greetingscard.yearNextAutoArchiving', '');