package at.redcross.tacos.dbal.generator;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class SchemeGenerator {

	public static void main(String[] args) {
		AnnotationConfiguration config = new AnnotationConfiguration();
		config.configure();
		SchemaExport schemaExport = new SchemaExport(config);
		schemaExport.create(true, true);
	}

}
