package at.redcross.tacos.tests.parser;

import java.io.File;
import java.net.URL;

public abstract class BaseFileTest {

    /**
     * Returns a file instance for the given project-relative path.
     * 
     * @param path
     *            the desired relative resource
     * @return the file instance
     */
    protected File getFile(String path) throws Exception {
        URL base = BaseFileTest.class.getResource("/");
        File binDir = new File(base.toURI());
        return new File(binDir.getParentFile(), path);
    }

}
