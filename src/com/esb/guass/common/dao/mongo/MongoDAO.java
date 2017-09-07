package com.esb.guass.common.dao.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.esb.guass.common.constant.MongoConstant;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

/**
 * Mongo数据库操作类
 * @author wicks
 */
public class MongoDAO{

    /**
     * Mongo主客户端
     */
    private MongoClient mongoClient = null;
    
    /**
     * Mongon次要客户端
     */
    private MongoClient secondaryMongoClient = null;
    
    /**
     * 删除内容备份库
     */
    private String deletedTable = "tb_deleted";
    
    
    /**
     * 操作类实例
     */
    private static final MongoDAO instance = new MongoDAO();
    
    private MongoDAO()
    {
        if (mongoClient == null)
        {
            MongoClientOptions.Builder buide = new MongoClientOptions.Builder();
            buide.connectionsPerHost(MongoConstant.MAX_CONNECTIONS);
            buide.connectTimeout(MongoConstant.CONNECTION_TIMEOUT);
            buide.maxWaitTime(MongoConstant.THREAD_MAX_WAITTIME);
            buide.threadsAllowedToBlockForConnectionMultiplier(MongoConstant.THREAD_MAX_WAITNUM);
            buide.maxConnectionIdleTime(0);
            buide.maxConnectionLifeTime(0);
            buide.socketTimeout(0);
            buide.socketKeepAlive(true);
            MongoClientOptions myOptions = buide.build();
            
            MongoCredential credential = MongoCredential.createScramSha1Credential(MongoConstant.USERNAME, "admin", MongoConstant.PASSWORD.toCharArray());  
            List<MongoCredential> credentials = new ArrayList<MongoCredential>();  
            credentials.add(credential);
            ServerAddress serverAddress = new ServerAddress(MongoConstant.IP, MongoConstant.PORT);  
            
            try
            {
                mongoClient = new MongoClient(serverAddress, credentials, myOptions);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        if (secondaryMongoClient == null)
        {
            MongoClientOptions.Builder buide = new MongoClientOptions.Builder();
            buide.connectionsPerHost(MongoConstant.SECONDARY_MAX_CONNECTIONS);
            buide.connectTimeout(MongoConstant.CONNECTION_TIMEOUT);
            buide.maxWaitTime(MongoConstant.THREAD_MAX_WAITTIME);
            buide.threadsAllowedToBlockForConnectionMultiplier(MongoConstant.THREAD_MAX_WAITNUM);
            buide.maxConnectionIdleTime(0);
            buide.maxConnectionLifeTime(0);
            buide.socketTimeout(0);
            buide.socketKeepAlive(true);
            MongoClientOptions myOptions = buide.build();
            
            MongoCredential credential = MongoCredential.createScramSha1Credential(MongoConstant.USERNAME, "admin", MongoConstant.PASSWORD.toCharArray());  
            List<MongoCredential> credentials = new ArrayList<MongoCredential>();  
            credentials.add(credential);
            ServerAddress serverAddress = new ServerAddress(MongoConstant.IP, MongoConstant.PORT);  
            
            try
            {
            	secondaryMongoClient = new MongoClient(serverAddress, credentials, myOptions);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取实例
     * @return
     */
    public static MongoDAO getInstance()
    {
        return instance;
    }

    /**
     * 获取数据库
     * @param dbName
     * @return
     */
    public MongoDatabase getDatabase(String dbName)
    {
        return mongoClient.getDatabase(dbName);
    }

    /**
     * 获取表集合
     * @param dbName
     * @param collectionName
     * @return
     */
    public MongoCollection<Document> getCollection(String dbName, String collectionName)
    {
    	MongoDatabase db = getDatabase(dbName);
        return db.getCollection(collectionName);
    }
    
    /**
     * 插入数据
     * @param dbName
     * @param collectionName
     * @param doc
     * @return
     */
    public boolean insert(String dbName, String collectionName, Document doc)
    {
    	MongoCollection<Document> dbCollection = getCollection(dbName, collectionName);
        long num = dbCollection.count();
        dbCollection.insertOne(doc);
        if (dbCollection.count() - num > 0)
        {
            return true;
        }
        return false;
    }
    
    /**
     * 插入数据(次要)
     * @param dbName
     * @param collectionName
     * @param doc
     * @return
     */
    public boolean secondaryInsert(String dbName, String collectionName, Document doc)
    {
    	MongoCollection<Document> dbCollection = secondaryMongoClient.getDatabase(dbName).getCollection(collectionName);
        long num = dbCollection.count();
        dbCollection.insertOne(doc);
        if (dbCollection.count() - num > 0)
        {
            return true;
        }
        return false;
    }
    
    /**
     * 更新
     * @param dbName
     * @param collectionName
     * @param filter
     * @param update
     * @return
     */
    public UpdateResult update(String dbName, String collectionName, Bson filter, Document update) {  
    	MongoCollection<Document> dbCollection = getCollection(dbName, collectionName);
        UpdateResult result = dbCollection.replaceOne(filter, update);  
        return result;  
    } 
  

    /**
     * 删除
     * @param dbName
     * @param collectionName
     * @param filter
     */
    public void delete(String dbName, String collectionName, Bson filter) {
    	//备份数据
    	MongoCollection<Document> dbCollection = getCollection(dbName, collectionName);
        FindIterable<Document> iterables = dbCollection.find(filter);  
        MongoCursor<Document> cursor = iterables.iterator();  
        while (cursor.hasNext()) {
            insert(dbName, deletedTable, cursor.next().append("_table", collectionName));
        }
    	
    	//物理删除
    	dbCollection.deleteMany(filter);  
    }
    
    /**
     * 查询所有文档 
     * @param dbName
     * @param collectionName
     * @return
     */
    public List<Document> findAll(String dbName, String collectionName) {  
        List<Document> results = new ArrayList<Document>();
        MongoCollection<Document> dbCollection = getCollection(dbName, collectionName);
        FindIterable<Document> iterables = dbCollection.find();  
        MongoCursor<Document> cursor = iterables.iterator();  
        while (cursor.hasNext()) {  
            results.add(cursor.next());  
        }
        return results;  
    } 
    
    /**
     * 查询所有文档 
     * @param dbName
     * @param collectionName
     * @param sort
     * @return
     */
    public List<Document> findAll(String dbName, String collectionName, Bson sort) {  
        List<Document> results = new ArrayList<Document>();
        MongoCollection<Document> dbCollection = getCollection(dbName, collectionName);
        FindIterable<Document> iterables = dbCollection.find().sort(sort);
        MongoCursor<Document> cursor = iterables.iterator();  
        while (cursor.hasNext()) {  
            results.add(cursor.next());  
        }
        return results;  
    } 
    
    /**
     * 按条件查询
     * @param dbName
     * @param collectionName
     * @param filter
     * @return
     */
    public List<Document> findBy(String dbName, String collectionName, Bson filter) {
        List<Document> results = new ArrayList<Document>();
        MongoCollection<Document> dbCollection = getCollection(dbName, collectionName);
        FindIterable<Document> iterables = dbCollection.find(filter);  
        MongoCursor<Document> cursor = iterables.iterator();  
        while (cursor.hasNext()) {  
            results.add(cursor.next());  
        }  
        return results;  
    }
    
    /**
     * 按条件查询
     * @param dbName
     * @param collectionName
     * @param filter
     * @return
     */
    public List<Document> findBy(String dbName, String collectionName, Bson filter, Bson sort) {
        List<Document> results = new ArrayList<Document>();
        MongoCollection<Document> dbCollection = getCollection(dbName, collectionName);
        FindIterable<Document> iterables = dbCollection.find(filter).sort(sort);
        MongoCursor<Document> cursor = iterables.iterator();  
        while (cursor.hasNext()) {  
            results.add(cursor.next());  
        }  
        return results;  
    }  
    
    /**
     * 按条件查询
     * @param dbName
     * @param collectionName
     * @param filter
     * @return
     */
    public List<Document> findBy(String dbName, String collectionName, Bson filter,  int pageNum, int pageSize) {
        List<Document> results = new ArrayList<Document>();
        MongoCollection<Document> dbCollection = getCollection(dbName, collectionName);
        FindIterable<Document> iterables = dbCollection.find(filter).skip((pageNum-1)*pageSize).limit(pageSize);
        MongoCursor<Document> cursor = iterables.iterator();  
        while (cursor.hasNext()) {  
            results.add(cursor.next());  
        }  
        return results;  
    }  
  
    /**
     * 按条件查询
     * @param dbName
     * @param collectionName
     * @param filter
     * @return
     */
    public List<Document> findBy(String dbName, String collectionName, Bson filter, Bson sort,  int pageNum, int pageSize) {
        List<Document> results = new ArrayList<Document>();
        MongoCollection<Document> dbCollection = getCollection(dbName, collectionName);
        FindIterable<Document> iterables;
        if(pageSize > 0){
        	iterables = dbCollection.find(filter).sort(sort).skip((pageNum-1)*pageSize).limit(pageSize);
        } else {
        	iterables = dbCollection.find(filter).sort(sort);
        }
        MongoCursor<Document> cursor = iterables.iterator();
        while (cursor.hasNext()) {  
            results.add(cursor.next());  
        }  
        return results;  
    }  
    
    /**
     * 获取条数
     * @param dbName
     * @param collectionName
     * @param filter
     * @return
     */
    public long count(String dbName, String collectionName, Bson filter) {
        MongoCollection<Document> dbCollection = getCollection(dbName, collectionName);
        return dbCollection.count(filter);
    }  
}