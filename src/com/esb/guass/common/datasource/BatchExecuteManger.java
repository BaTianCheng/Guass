package com.esb.guass.common.datasource;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.esb.guass.common.util.LogUtils;

/**
 * 批量执行管理器
 * @author wicks
 */
public class BatchExecuteManger {
	
	/**
	 * 批量执行SQL语句
	 * @param connection
	 * @param sqls
	 * @throws SQLException 
	 */
	public static void batchExecute(Connection connection, List<String> sqls) throws SQLException {
		try{
			connection.setAutoCommit(false);
			final Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int count = 0;
			for (String sql : sqls) {
	        	stmt.addBatch(sql);
	        	count++;
	        	if(count %5000==0){
	        		stmt.executeBatch();
	        	}
	        }
			stmt.executeBatch();
			connection.commit();
			stmt.close();
			connection.setAutoCommit(true);
			connection.close();
		 } catch (SQLException e) {
			 if(e instanceof BatchUpdateException){
				int no = ((BatchUpdateException) e).getUpdateCounts().length;
				LogUtils.info("第"+no+"条语句出错，"+sqls.get(no));
			 }
			connection.rollback();
	        throw new RuntimeException(e);
	    }
	}

}
