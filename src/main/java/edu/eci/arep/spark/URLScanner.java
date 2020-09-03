/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arep.spark;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrés Quintero
 */
public class URLScanner {

    public static void main(String[] args) {

        URLScanner("https://ldbn.is.escuelaing.edu.co/AREP/Respuestas.txt");
        URLScanner("https://ldbn.is.escuelaing.edu.co:6008/AREP/Respuestas.txt");
        URLScanner("https://www.google.com/search?q=covid+colombia&oq=covid+colombia&aqs=chrome..69i57j0l7.7653j0j7&sourceid=chrome&ie=UTF-8#wptab=s:H4sIAAAAAAAAAONgVuLVT9c3NMwySk6OL8zJecTYwMgt8PLHPWGp8klrTl5jLOQS901NyUzOzEt1ySxOTSxO9clPTizJzM8T0uNic80rySypFFLhEpRCNUeDQYqfC1VISI2LA65XiotHikM_V9_AMKfYCKiYiwvO49nFxO2RmphTkhFcklhSvIhVI7W4JDHl8NrikszkxGKFlFQF5_yi_LzEssyi0mKF1DwgNyc_NykzEQAimFObzgAAAA");
        URLScanner("https://ldbn.is.escuelaing.edu.co/events/ArqIS2020/index.html#TemasC");
    }

    public static void URLScanner(String siteurl) {
        try {
            URL site = new URL(siteurl);
            System.out.println(site);
            System.out.println("============================");
            System.out.println("Protocol: " + site.getProtocol());
            System.out.println("Host: " + site.getHost());
            System.out.println("Port: " + site.getPort());
            System.out.println("Path: " + site.getPath());
            System.out.println("File: " + site.getFile());
            System.out.println("Query: " + site.getQuery());
            System.out.println("Referencia: " + site.getRef());
            System.out.println("Authority: " + site.getAuthority());
        } catch (MalformedURLException ex) {
            Logger.getLogger(URLScanner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
