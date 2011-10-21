package at.redcross.tacos.web.reporting;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.core.framework.PlatformFileContext;
import org.eclipse.birt.core.framework.PlatformServletContext;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.redcross.tacos.web.config.SettingsStore;

public class ReportRenderer {

    private final static Logger logger = LoggerFactory.getLogger(ReportRenderer.class);

    /** The one and only instance */
    private static ReportRenderer renderer;

    /** the engine to render the reports */
    private IReportEngine engine;

    /**
     * Creates and initializes the reporting engine
     */
    private ReportRenderer() {
        initEngine();
    }

    /**
     * Returns the shared reporting renderer instance.
     * 
     * @return the report renderer
     */
    public static synchronized ReportRenderer getInstance() {
        if (renderer == null) {
            renderer = new ReportRenderer();
        }
        return renderer;
    }

    /**
     * Returns the shared reporting engine
     * 
     * @return the engine
     */
    public IReportEngine getEngine() {
        return engine;
    }

    /**
     * Request to generate and serve a new report using the given parameters.
     * The report will be written to the current response.
     * 
     * @param params
     *            the parameters for the report
     * @throws Exception
     *             if the report generation failed
     */
    public void renderReport(ReportRenderParameters params) throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext extContext = facesContext.getExternalContext();

        // response should be a PDF file
        HttpServletResponse resp = (HttpServletResponse) extContext.getResponse();
        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + params.reportName + "\"");

        InputStream stream = null;
        IRunAndRenderTask task = null;
        try {
            long start = System.currentTimeMillis();
            logger.info("Creating report '" + params.reportName + "'");

            stream = extContext.getResourceAsStream("report/" + params.reportFile);

            // get the engine and pass the parameters
            IReportEngine engine = ReportRenderer.getInstance().getEngine();
            if (engine == null) {
                throw new RuntimeException("Reporting engine not available");
            }
            IReportRunnable design = engine.openReportDesign(stream);

            // set output options
            PDFRenderOption options = new PDFRenderOption();
            options.setOutputFormat(PDFRenderOption.OUTPUT_FORMAT_PDF);
            options.setOutputStream(resp.getOutputStream());

            task = engine.createRunAndRenderTask(design);
            task.setRenderOption(options);
            for (Map.Entry<String, Object> entry : params.arguments.entrySet()) {
                task.setParameterValue(entry.getKey(), entry.getValue());
            }

            // run PDF generation
            task.run();
            long duration = System.currentTimeMillis() - start;
            logger.info("Finished report generation in '" + duration + "'ms");
        } finally {
            // cleanup resources
            IOUtils.closeQuietly(stream);
            if (task != null) {
                task.close();
            }
            facesContext.responseComplete();
        }
    }

    /** Does the needed initialization */
    private void initEngine() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext extContext = facesContext.getExternalContext();
        ServletContext servletContext = (ServletContext) extContext.getContext();

        try {
            EngineConfig config = new EngineConfig();
            config.setResourcePath(servletContext.getRealPath("/"));

            // check the report engine directory
            File reportEngine = new File(SettingsStore.getInstance().getHome(), "report-engine");
            if (reportEngine.isDirectory()) {
                PlatformConfig platformConfig = new PlatformConfig();
                platformConfig.setBIRTHome(reportEngine.getAbsolutePath());
                config.setEngineHome(reportEngine.getAbsolutePath());
                config.setPlatformContext(new PlatformFileContext(platformConfig));
                logger.info("Using file reporting engine");
            }

            // Using the PlatformServletContext will cause the OSGi loader to
            // look for the plug-ins in the WEB-INF/platform directory.
            if (!reportEngine.isDirectory()) {
                config.setEngineHome("");
                config.setPlatformContext(new PlatformServletContext(servletContext));
                logger.info("Using servlet reporting engine");
            }

            // request to startup the OSGi-framework
            Platform.startup(config);

            // get the report factory and cache the engine
            IReportEngineFactory factory = (IReportEngineFactory) Platform
                    .createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
            engine = factory.createReportEngine(config);
            logger.info("Using reporting engine '" + engine.getVersion() + "'");
        } catch (Exception ex) {
            logger.error("Failed to initialize the reporting engine.", ex);
        }
    }

    public static class ReportRenderParameters {

        public String reportName;
        public String reportFile;
        public Map<String, Object> arguments = new HashMap<String, Object>();
    }

}
