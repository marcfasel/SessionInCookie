package au.com.marcsworld.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.Logger;

/**
 * The FilterResponseWrapper wraps a servlet response to allow explicit control over when a 
 * response is committed. Only calling flushBuffer() on this class will commit the response.
 * 
 * @author marcfasel
 *
 */
public class FilterResponseWrapper extends HttpServletResponseWrapper {
	Logger LOGGER = Logger.getLogger(FilterResponseWrapper.class);

    private final WrappedServletOutputStream output;
    private final PrintWriter writer;

    public FilterResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);
        output = new WrappedServletOutputStream(response.getOutputStream());
        writer = new PrintWriter(output, true);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
       	LOGGER.debug("getOutputStream()");
        return output;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
       LOGGER.debug("getWriter()");
       return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
       	LOGGER.debug("flushBuffer()");
        writer.flush();
    }

}
