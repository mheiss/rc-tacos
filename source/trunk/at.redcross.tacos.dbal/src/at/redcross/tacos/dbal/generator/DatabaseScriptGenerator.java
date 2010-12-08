package at.redcross.tacos.dbal.generator;

import java.io.File;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.DefaultComponentSafeNamingStrategy;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import at.redcross.tacos.dbal.utils.EntityUtils;

/**
 * The {@code DdlScriptGenerator} generates DDL script file from annotated
 * entities.
 * 
 * <pre>
 * Credits go to:
 * http://jandrewthompson.blogspot.com/2009/10/how-to-generate-ddl-scripts-from.html
 * </pre>
 */
public class DatabaseScriptGenerator {

    /**
     * Holds the different <tt>dialects</tt> that are supported
     */
    enum Dialect {
        /** MS-SQL */
        MSSQL("org.hibernate.dialect.SQLServerDialect"),
        /** ORACLE 10 */
        ORACLE("org.hibernate.dialect.Oracle10gDialect"),
        /** MySQL */
        MYSQL("org.hibernate.dialect.MySQLDialect"),
        /** HSQL */
        HSQL("org.hibernate.dialect.HSQLDialect");

        private String dialectClass;

        private Dialect(String dialectClass) {
            this.dialectClass = dialectClass;
        }

        public String getDialectClass() {
            return dialectClass;
        }
    }

    /** where will the scripts be placed */
    private File outputDir = new File(".", "scripts");

    /** look for classes in this package **/
    private String packageName = "at.redcross.tacos.dbal.entity";

    /** holds all found classes */
    private Configuration cfg;

    public static void main(String[] args) throws Exception {
        DatabaseScriptGenerator gen = new DatabaseScriptGenerator();
        gen.generate(Dialect.MSSQL);
        gen.generate(Dialect.MYSQL);
        gen.generate(Dialect.ORACLE);
        gen.generate(Dialect.HSQL);
        System.out.println("Generation finished.");
    }

    public DatabaseScriptGenerator() throws Exception {
        initGenerator();
    }

    /**
     * Method that actually creates the file.
     * 
     * @param dbDialect
     *            to use
     */
    private void generate(Dialect dialect) {
        cfg.setProperty("hibernate.dialect", dialect.getDialectClass());
        cfg.buildMappings();
        AuditConfiguration.getFor(cfg);
        SchemaExport export = new SchemaExport(cfg);
        export.setFormat(true);
        export.setDelimiter(";");
        // CREATE scripts
        String dialectName = dialect.name().toLowerCase();
        System.out.println("Generating scripts for '" + dialectName + "' ...");
        {
            File out = new File(outputDir, "create_" + dialectName + ".sql");
            export.setOutputFile(out.getAbsolutePath());
            export.execute(false, false, false, true);
        }
        // DROP scripts
        {
            File out = new File(outputDir, "drop_" + dialectName + ".sql");
            export.setOutputFile(out.getAbsolutePath());
            export.execute(false, false, true, false);
        }

    }

    /** initializes the generator */
    private void initGenerator() throws Exception {
        cfg = new Configuration();
        cfg.setNamingStrategy(new DefaultComponentSafeNamingStrategy());
        cfg.setProperty("hibernate.hbm2ddl.auto", "create");
        for (Class<?> clazz : EntityUtils.listEntityClasses(packageName)) {
            cfg.addAnnotatedClass(clazz);
        }
        if (!outputDir.isDirectory()) {
            outputDir.mkdirs();
        }
    }

}
