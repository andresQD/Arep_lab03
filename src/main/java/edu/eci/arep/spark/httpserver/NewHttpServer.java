/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arep.spark.httpserver;

import edu.eci.arep.spark.persistencia.ConexionMongo;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
/**
 *
 * @author Andrés Quintero
 */
public class NewHttpServer {
   
    
    private boolean running = false;
    ConexionMongo cm;

    public NewHttpServer() {
    }
    
    public NewHttpServer(ConexionMongo cm) {
        this.cm = cm;
    }

    

    /**
     * Start.
     */
    public void start() {
        try {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(getPort());
            } catch (IOException e) {
                System.err.println("not listen on port: " + getPort());
                System.exit(1);
            }
            running = true;
            while (running) {
                try {
                    Socket clientSocket = null;
                    try {
                        System.out.println("Listening on port... "+getPort());
                        clientSocket = serverSocket.accept();
                    } catch (IOException e) {
                        System.err.println("Accept failed.");
                        System.exit(1);
                    }
                    processRequest(clientSocket);
                    clientSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(NewHttpServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(NewHttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processRequest(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        Map<String, String> request = new HashMap<>();
        boolean requestLineReady = false;
        while ((inputLine = in.readLine()) != null) {
            if (!requestLineReady) {
                request.put("requestLine", inputLine);
                requestLineReady = true;
            } else {
                String[] entry = createEntry(inputLine);
                if (entry.length > 1) {
                    request.put(entry[0], entry[1]);
                }
            }
            if (!in.ready() || inputLine.length()==0) {
                break;
            }
        }
        System.out.println(request);
        if (request.get("requestLine") !=null){
            Request req = new Request(request.get("requestLine"));
            System.out.println("RequestLine: " + req);
            createResponse(req,
                    clientSocket.getOutputStream());
            in.close();
        }

    }

    private String[] createEntry(String rawEntry) {
        return rawEntry.split(":");
    }

    private void createResponse(Request req, OutputStream out) throws IOException {
        URI theuri = req.getTheuri();
        System.out.println("METHOOOD");
        System.out.println(req.getMethod());
        getStaticResource(theuri.getPath(),out);
        out.close();
    }

    private void getStaticResource(String path, OutputStream out) throws IOException {
        if (path.equals("/")) {
            path = "/index.html";
        }
        if(path.contains("/Apps")){
            path = path.replace("/Apps", "");
        }
        Path file = Paths.get("src/main/resources" + path);
        File arch = new File(System.getProperty("user.dir")+"/"+file);
        
        if (arch.exists()) {
            String tipo = Files.probeContentType(file);
            if (tipo.startsWith("text/")) {
                getFile(out, file, tipo.substring(5));
            } else if (tipo.startsWith("image/")) {
                getImage(out, path, tipo.substring(6) );
            }
        }else{
            file = Paths.get("src/main/resources/noEncuentra.html");
            notFound(out,file);
        }
    }

    private void notFound(OutputStream out,Path file){
        PrintWriter response = new PrintWriter(out,true);
        try (InputStream in = Files.newInputStream(file);
             BufferedReader reader
                     = new BufferedReader(new InputStreamReader(in))) {
            String header = "HTTP/1.1 404 Not Found \r\n"
                    + "Content-Type: text/html \r\n"
                    + "\r\n";
            response.println(header);
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.println(line);
                System.out.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(NewHttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getFile(OutputStream out,Path file,String ext){
        PrintWriter response = new PrintWriter(out,true);
        try (InputStream in = Files.newInputStream(file);
             BufferedReader reader
                     = new BufferedReader(new InputStreamReader(in))) {
            String header = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/"+ext+"\r\n"
                    + "\r\n";
            response.println(header);
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.println(line);
                System.out.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(NewHttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private void getImage(OutputStream out, String resource, String ext) throws IOException,IIOException {
        PrintWriter response = new PrintWriter(out, true);
        ByteArrayOutputStream by = new ByteArrayOutputStream();
        File file = new File(System.getProperty("user.dir") + "/src/main/resources" + resource);
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (Exception e) {
            System.out.println("NOT FOUND");
        }
        System.out.println("file " + file.getName());
        System.out.println("LENGHT " + file.length());
        response.println("HTTP/1.1 200 OK\r\n" +
                "Content-Type: image/" + ext + "\r\n");
        try {
            ImageIO.write(image, ext, out);
            out.close();
        } catch (IOException e) {
            System.out.println("not found");
        }
    }
    
  
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 36000; 
    }

 

    /**private String testResponse() {
        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Title of the document</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "<h1>Mi propio mensaje</h1>\n"
                + "</body>\n"
                + "</html>\n";
        return outputLine;
    }**/
    
/**
    private void invokeApp(String appuri, PrintWriter out) {
            String header = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n";
            
            
    }**/
}
