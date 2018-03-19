package com.boris.common.transactional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionalService {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	/**
	 * 数据库事务状态
	 */
	protected static final ThreadLocal<TransactionStatus> mTranStatus = new ThreadLocal<TransactionStatus>();
	
	/**
	 * 类DataSourceTransactionManager的实例
	 */
	@Autowired
	protected DataSourceTransactionManager mDataSourceTransManager;

	/**
	 * 准备数据库事务：每次创建新的事务会重新选择数据库来创建SQLSessoion而不会从现有SQLSession中选择，这样确保事务连接的是正确的数据库
	 */
	protected void prepareTransaction() {
		if (mDataSourceTransManager == null) return;
			
		try {
			DefaultTransactionDefinition defaultTransDefine = new DefaultTransactionDefinition();
			defaultTransDefine.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			
			TransactionStatus ts = mDataSourceTransManager.getTransaction(defaultTransDefine);
			mTranStatus.set(ts);
		} catch (Exception e) {
			LOG.info("开启事务异常",e);
		}
	}
	
	/**
	 * 提交数据库事务
	 */
	protected void commitTransaction() {
		try {
			TransactionStatus ts = mTranStatus.get();
			if (ts != null) {
				mDataSourceTransManager.commit(ts);
			}
			mTranStatus.set(null);
		} catch (Exception e) {
			LOG.info("提交事务异常",e);
		}
	}
	
	/**
	 * 回滚数据库事务
	 */
	protected void rollbackTransaction() {
		try {
			TransactionStatus ts = mTranStatus.get();
			if (ts != null) {
				mDataSourceTransManager.rollback(ts);
			}
			mTranStatus.set(null);
		} catch (Exception e) {
			LOG.info("回滚事务异常",e);
		}
	}

}
