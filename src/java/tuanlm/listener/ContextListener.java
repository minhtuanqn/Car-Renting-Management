/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuanlm.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String fileName = sc.getRealPath("/") + "WEB-INF" + "\\ResourceFile.txt";
        Map<String , String> map = readFile(fileName);
        sc.setAttribute("MAP", map);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
    
    private Map<String, String> readFile(String fileName) {
        Map<String, String> map = new HashMap();
        FileReader fr = null;
        BufferedReader br = null;
        try {
            File f = new File(fileName);
            if(!f.exists()) {
                f.createNewFile();
            }
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String detail = null;
            while((detail = br.readLine()) != null) {
                String [] details = detail.split("=");
                if(details != null && details.length == 2) {
                    map.put(details[0], details[1]);
                }
            }
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(ContextListener.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) {
            Logger.getLogger(ContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            
            try {
                if(br != null) {
                    br.close();
                }
                if(fr != null) {
                    fr.close();
                }
            } 
            catch (IOException ex) {
               Logger.getLogger(ContextListener.class.getName()).log(Level.SEVERE, null, ex); 
            }
        }
        return map;
        
    }
}
