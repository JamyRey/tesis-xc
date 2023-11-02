package org.tesis.xc.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class PropertiesUtil {
  public static Properties getRecursoClassPath(String url) {
    Properties ht = new Properties();
    try {
      ht.load(PropertiesUtil.class.getResourceAsStream(url));
    } catch (Throwable e) {
      e.printStackTrace();
    } 
    return ht;
  }
  
  public static Properties getRecursoURLPath(String url) {
    Properties ht = new Properties();
    try {
      ht.load(new FileInputStream(url));
    } catch (Throwable e) {
      e.printStackTrace();
    } 
    return ht;
  }
  
  public static void save(Properties p, String fileName) {
    SimpleDateFormat sf = null;
    try {
      OutputStream outPropFile = new FileOutputStream(fileName);
      sf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss a");
      p.store(outPropFile, "Desimplex - " + sf.format(new Date()));
      outPropFile.close();
    } catch (IOException ioe) {
      System.out.println("I/O Exception.");
      ioe.printStackTrace();
      System.exit(0);
    } finally {
      OutputStream outPropFile = null;
      sf = null;
    } 
  }
}