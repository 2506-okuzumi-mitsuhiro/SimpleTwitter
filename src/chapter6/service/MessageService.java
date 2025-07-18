package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.beans.UserMessage;
import chapter6.dao.MessageDao;
import chapter6.dao.UserMessageDao;
import chapter6.logging.InitApplication;

public class MessageService {


	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public MessageService() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	public void insert(Message message) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			connection = getConnection();
			new MessageDao().insert(connection, message);
			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);

			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);

			throw e;
		} finally {
			close(connection);
		}
	}

	// WEB開発基礎課題（つぶやきの絞り込み）
	// 実践課題 その②修正ヵ所
	// 表示するユーザのIDを引数として追加
	public List<UserMessage> select(String userId, String inputStartDate, String inputEndDated) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		final int LIMIT_NUM = 1000;

		Connection connection = null;
		try {
			connection = getConnection();

			// 実践課題 その②修正ヵ所
			// 表示するユーザのIDを型変換しInteger型でUserMessageDao().selectに渡すよう修正
			// 全件抽出時にも同メソッドを使用する為、最初にnullを代入しておく
			Integer id = null;

			if(!StringUtils.isEmpty(userId)) {
				id = Integer.parseInt(userId);
			}

			// WEB開発基礎課題（つぶやきの絞り込み）
			String startDate = null;
			String endDated = null;

			if(!StringUtils.isBlank(inputStartDate)) {
				startDate = inputStartDate + " 00:00:00";
			}else {
				startDate = "2020-01-01 00:00:00";
			}

			if(!StringUtils.isBlank(inputEndDated)) {
				endDated = inputEndDated + " 23:59:59";
			}else {
				// 現在時刻の取得
				Date nowDate = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				endDated = dateFormat.format(nowDate);
			}

			List<UserMessage> messages = new UserMessageDao().select(connection, id, LIMIT_NUM, startDate, endDated);
			commit(connection);

			return messages;
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);

			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);

			throw e;
		} finally {
			close(connection);
		}
	}

	// WEB開発基礎課題（つぶやきの削除）
	// 引数で受け取ったmessageIdをDaoに渡す
	public void delete(int messageId) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			connection = getConnection();
			new MessageDao().delete(connection, messageId);
			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);

			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);

			throw e;
		} finally {
			close(connection);
		}
	}

	// WEB開発基礎課題（つぶやきの編集）
	public Message select(int editMessagesId) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		Connection connection = null;

		try {
			connection = getConnection();
			Message message = new MessageDao().select(connection, editMessagesId);
			commit(connection);

			return message;
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);

			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);

			throw e;
		} finally {
			close(connection);
		}
	}

	public void update(Message message) {
		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			connection = getConnection();
			new MessageDao().update(connection, message);
			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);

			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);

			throw e;
		} finally {
			close(connection);
		}
	}
}
