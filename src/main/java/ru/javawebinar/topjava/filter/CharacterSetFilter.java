package ru.javawebinar.topjava.filter;

import javax.servlet.*;
import java.io.IOException;

public class CharacterSetFilter implements Filter {

    private static final String UTF8 = "UTF-8";
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("requestCharEncoding");
        if (encoding == null) {
            encoding = UTF8;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // Honour the client-specified character encoding
        if (null == servletRequest.getCharacterEncoding()) {
            servletRequest.setCharacterEncoding(encoding);
        }
        /**
         * Set the default response content type and encoding
         */
        servletResponse.setContentType(CONTENT_TYPE);
        servletResponse.setCharacterEncoding(UTF8);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
