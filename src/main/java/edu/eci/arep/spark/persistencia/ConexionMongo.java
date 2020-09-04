/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arep.spark.persistencia;

/**
 *
 * @author Andrés Quintero
 */
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.*;

public class ConexionMongo {

    private DBCollection collection;
    private DB db;

    public ConexionMongo() {
        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://andresQD:arep2020@cluster0.xpuez.mongodb.net/andresQD?retryWrites=true&w=majority");
        MongoClient mongoClient = new MongoClient(uri);
        //MongoDatabase database = mongoClient.getDatabase("test");
        db = mongoClient.getDB("arep");
        collection = db.getCollection("arep");
        MongoDatabase database = mongoClient.getDatabase("arep");
        MongoCollection<Document> collection = database.getCollection("arep");
        /**Document document1 = new Document("name", "Ram").append("age", 26).append("city", "Hyderabad");
        Document document2 = new Document("name", "Robert").append("age", 27).append("city", "Vishakhapatnam");
        Document document3 = new Document("name", "Rhim").append("age", 30).append("city", "Delhi");
        //Inserting the created documents
        List<Document> list = new ArrayList<Document>();
        list.add(document1);
        list.add(document2);
        list.add(document3);
        collection.insertMany(list);
        System.out.println("Documents Inserted");**/
        FindIterable<Document> cursor = collection.find();
            for(Document document : cursor) {
               System.out.println(document);
        }

    }

    public void add() {
        BasicDBList object = new BasicDBList();
        //object.put("nombre",name);
    }

    public ArrayList<BasicDBObject> select() {
        ArrayList<BasicDBObject> registros = new ArrayList<BasicDBObject>();

        return registros;
    }
}
