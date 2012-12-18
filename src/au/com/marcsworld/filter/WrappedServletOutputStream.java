package au.com.marcsworld.filter;

import java.io.FilterOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;

/**
 * The WarppedServletOutputStream acts as a wrapper around an output stream to control
 * flushing of the stream.
 * 
 * @author marcfasel
 *
 */
public class WrappedServletOutputStream extends ServletOutputStream {
	Logger LOGGER = Logger.getLogger(WrappedServletOutputStream.class);

    private final FilterOutputStream output;

    public WrappedServletOutputStream(ServletOutputStream output) {
        this.output = new FilterOutputStream(output);
    }

    @Override
    public void write(int b) throws IOException {
      	//LOGGER.debug("write()");
        output.write(b);
    }

    @Override
    public void flush() throws IOException {
     	LOGGER.debug("flush()");
        output.flush();
    }

}
