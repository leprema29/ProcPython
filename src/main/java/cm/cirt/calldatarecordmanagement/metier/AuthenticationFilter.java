/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.metier;

import static cm.cirt.calldatarecordmanagement.metier.Variables111.AUTH_KEY;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Harry Wanki
 */
@WebFilter("/faces/*")
public class AuthenticationFilter implements Filter {

    private FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String reqURI = req.getRequestURI();
        if ((reqURI.contains("listing")) || (reqURI.contains("home.xhtml"))
                || (reqURI.contains("welcomePrimefaces.xhtml"))
                || (reqURI.contains("download.xhtml"))
                || (reqURI.contains("change.xhtml"))
                || (reqURI.contains("historique_listing.xhtml"))
                || (reqURI.contains("user"))
                || (reqURI.contains("edjoeikvdwil24kdl"))
                || (reqURI.contains("identification.xhtml"))) {
            if (((HttpServletRequest) request).getSession().getAttribute(AUTH_KEY) == null) {
                ((HttpServletResponse) response).sendRedirect("../faces/login.xhtml");
            } else if (reqURI.contains("login.xhtml")) {
                ((HttpServletResponse) response).sendRedirect("../faces/home.xhtml");
            } else {
                chain.doFilter(request, response);
            }
        } else if (reqURI.contains("image")) {
            if (((HttpServletRequest) request).getSession().getAttribute(AUTH_KEY) == null) {
                ((HttpServletResponse) response).sendRedirect("../../../../faces/home.xhtml");
            } else {
                chain.doFilter(request, response);
            }
        } else if (reqURI.contains("login.xhtml")) {
            if (((HttpServletRequest) request).getSession().getAttribute(AUTH_KEY) == null) {
                chain.doFilter(request, response);
            } else {
                ((HttpServletResponse) response).sendRedirect("../faces/home.xhtml");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        this.config = null;
    }
}
