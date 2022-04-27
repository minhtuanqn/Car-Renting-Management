/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuamlm.filter;

import java.io.IOException;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author MINH TUAN
 */
public class DispatcherFilter implements Filter {
    
    private static final boolean debug = true;

    private FilterConfig filterConfig = null;
    
    public DispatcherFilter() {
    }    
    
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        ServletContext sc = request.getServletContext();
        Map<String, String> map = (Map<String, String>) sc.getAttribute("MAP");
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();
        String url = "";
        try {
            int lastIndex = uri.lastIndexOf("/");
            String resource = uri.substring(lastIndex + 1);
            url = map.get(resource);
            if(url != null) {
                
            }
            else {
                url = "notfound.jsp";
            }
            RequestDispatcher rd = req.getRequestDispatcher(url);
            rd.forward(request, response);
        } 
        catch (Exception e) {
            log("DispatcherFilter: " + e.getMessage());
        }
        
    }

    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }


    public void destroy() {        
    }


    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {                
                log("DispatcherFilter:Initializing filter");
            }
        }
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);        
    }
    
}
