/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arep.spark.httpserver;

import edu.eci.arep.spark.persistencia.ConexionMongo;

/**
 *
 * @author Andrés Quintero
 */
public class HTTPServer {

    public static void main(String[] args) {
        ConexionMongo cm = new ConexionMongo();
        NewHttpServer server = new NewHttpServer(cm);
        server.start();
    }
}
