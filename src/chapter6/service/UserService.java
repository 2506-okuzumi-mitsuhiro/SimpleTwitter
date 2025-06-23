package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.User;
import chapter6.dao.UserDao;
import chapter6.logging.InitApplication;
import chapter6.utils.CipherUtil;

public class UserService {


	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public UserService() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	public void insert(User user) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			// パスワード暗号化
			String encPassword = CipherUtil.encrypt(user.getPassword());
			user.setPassword(encPassword);

			connection = getConnection();
			new UserDao().insert(connection, user);
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

	public User select(String accountOrEmail, String password) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			// パスワード暗号化
			String encPassword = CipherUtil.encrypt(password);

			connection = getConnection();
			User user = new UserDao().select(connection, accountOrEmail, encPassword);
			commit(connection);

			return user;
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

	public User select(int userId) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			connection = getConnection();
			User user = new UserDao().select(connection, userId);
			commit(connection);

			return user;
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

	public void update(User user) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			// 実践課題 その①修正ヵ所
			// パスワードの入力がある場合は暗号化
			String userPassword = user.getPassword();

			if (!StringUtils.isBlank(userPassword)) {
				userPassword = CipherUtil.encrypt(userPassword);
			}

			user.setPassword(userPassword);

			connection = getConnection();
			new UserDao().update(connection, user);
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

	// 実践課題 その③修正ヵ所
	// 受け取ったアカウント情報をDaoに渡しデータ抽出を行い、結果をUserで返す
	// 重複あり：抽出結果
	// 重複なし：null
	public User select(String account) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		Connection connection = null;

		try {
			connection = getConnection();
			User user = new UserDao().select(connection, account);
			commit(connection);

			return user;
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
