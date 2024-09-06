package com.venned.simplecorepvp.utils.data;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Collections;

public class MongoConnection {

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public MongoConnection(String host, int port, String databaseName, String username, String password) {
        MongoCredential credential = MongoCredential.createScramSha256Credential(username, databaseName, password.toCharArray());
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            mongoClient = new MongoClient(new ServerAddress(host, port), credential, MongoClientOptions.builder().build());
        } else {
            mongoClient = new MongoClient(new ServerAddress(host, port));
        }
        database = mongoClient.getDatabase(databaseName);
        collection = database.getCollection("playerData");
    }

    /*
       public MongoConnection(String host, int port, String databaseName, String username, String password) {
        MongoCredential credential = MongoCredential.createScramSha256Credential(username, databaseName, password.toCharArray());
        mongoClient = new MongoClient(new ServerAddress(host, port), credential, MongoClientOptions.builder().build());
        database = mongoClient.getDatabase(databaseName);
        collection = database.getCollection("playerData");
    }
     */

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}
