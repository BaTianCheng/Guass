package com.esb.guass.test;

import java.sql.Connection;
import java.sql.DriverManager;

import org.redkale.source.DataSource;

import com.esb.guass.common.datasource.DataSourceManger;
import com.esb.guass.common.datasource.TransactionManger;

public class MongoTest {
	public static void main(String[] args) {
		/*
		 * Document document = new Document(); document.append("name",
		 * "wang").append("gender", "female"); MongoDb.insert(document);
		 * 
		 * Document filter = new Document(); filter.append("name", "li si");
		 * List<Document> results = MongoDb.findBy(filter);
		 * 
		 * Document filter = new Document(); filter.append("gender", "male");
		 * //注意update文档里要包含"$set"字段 Document update = new Document();
		 * update.append("$set", new Document("gender", "female")); UpdateResult
		 * result = MongoDb.updateOne(filter, update);
		 * 
		 * Document filter = new Document(); filter.append("name", "li");
		 * MongoDb.deleteOne(filter);
		 * 
		 */
		/*
		 * RequestEntity requestEntity = new RequestEntity(); RequestOption
		 * option = new RequestOption();
		 * requestEntity.setQuestId(UUID.randomUUID().toString());
		 * option.setBody(true); requestEntity.setRequestOption(option);
		 * Document documentA = new
		 * Document(JSONObject.parseObject(requestEntity.toString())) ;
		 * System.out.println(requestEntity.toString());
		 * System.out.println(JSONObject.parseObject(requestEntity.toString()));
		 * System.out.println(documentA);
		 */
		/*
		 * Document document = new Document(); document.append("name",
		 * "wang").append("gender", "female"); Document document2 = new
		 * Document(); document2.append("name", "wanga");
		 * document2.append("obj", document);
		 * MongoDAO.getInstance().insert("db_ebs", "tb_request",document2);
		 * 
		 * System.out.println(MongoDAO.getInstance().findAll("db_ebs",
		 * "tb_request"));
		 */
		// System.out.println(RequestService.findAll().get(0).getQuestId());
		// System.out.println(RequestService.find("51c965bb-f01f-40ae-b4f6-70fbc28d6a01").getResult());
		// RequestCondition c = new RequestCondition();
		// c.setStatus("1203");
		// System.out.println(RequestService.findPages(c));

		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@192.168.70.10:1521:orcltest";
		String username = "rgcms";
		String password = "oratest";
		@SuppressWarnings("unused")
		Connection conn = null;
		try {
			Class.forName(driver); // classLoader,加载对应驱动
			conn = (Connection) DriverManager.getConnection(url, username, password);
			// PreparedStatement pstmt;
			// pstmt = (PreparedStatement) conn.prepareStatement("select
			// current_billno from s_billno_current_pub t where t.bill_type =
			// 'CRD_REGISTER_IDCARD' and t.params='-' for update");
			// ResultSet rs = pstmt.executeQuery();
			// pstmt = (PreparedStatement) conn.prepareStatement("update
			// s_billno_current_pub t set t.current_billno = '0000000293' where
			// t.bill_type ='CRD_REGISTER_IDCARD' and t.params ='-'");
			// pstmt.execute();
			//
			// rs.close();
			// pstmt.close();
			// conn.close();
			DataSource ds = DataSourceManger.getDataSource("RGCMS");
			TransactionManger t = new TransactionManger();
			t.beigin(ds.createReadSQLConnection());
			t.query("select  current_billno  from  s_billno_current_pub t where t.bill_type = 'CRD_REGISTER_IDCARD' and t.params='-' for update");
			t.commit();

		} catch(Exception e) {
			e.printStackTrace();
		}

		while(true) {

		}

	}
}
