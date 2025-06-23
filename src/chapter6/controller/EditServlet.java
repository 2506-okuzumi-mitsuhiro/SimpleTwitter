package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

// WEB開発基礎課題（つぶやきの編集）
@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public EditServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	// requestから取得したeditMessageIdをMessageService().selectの引数として渡す為にintに型変換
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		String stringEditMessageId = request.getParameter("editMessageId");

		List<String> errorMessages = new ArrayList<String>();
		Message editMessage = null;

		// 編集対象のIDの指定があるか確認
		if(stringEditMessageId != null) {
			// 編集対象のIDが数字となっているか確認
			if(stringEditMessageId.matches("^[0-9]{1,}$")) {
				int intEditMessageId = Integer.parseInt(stringEditMessageId);

				editMessage = new MessageService().select(intEditMessageId);
			}
		}

		// 編集対象はDBに存在するか確認
		if(editMessage == null) {
			HttpSession session = request.getSession();
			errorMessages.add("不正なパラメータが入力されました");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");

			return;
		}

		request.setAttribute("editMessage", editMessage);
		request.getRequestDispatcher("/edit.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		List<String> errorMessages = new ArrayList<String>();

		Message message = getMessage(request);
		if (!isValid(message, errorMessages)) {
			request.setAttribute("errorMessages", errorMessages);
			request.setAttribute("editMessage", message);
			request.getRequestDispatcher("/edit.jsp").forward(request, response);
			return;
		}

		new MessageService().update(message);

		response.sendRedirect("./");
	}

	private Message getMessage(HttpServletRequest request) throws IOException, ServletException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		Message message = new Message();
		message.setId(Integer.parseInt(request.getParameter("editMessageId")));
		message.setText(request.getParameter("editMessageText"));

		return message;
	}

	private boolean isValid(Message message, List<String> errorMessages) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		String text = message.getText();

		if (StringUtils.isBlank(text)) {
			errorMessages.add("メッセージを入力してください");
		} else if (140 < text.length()) {
			errorMessages.add("140文字以下で入力してください");
		}

		if (errorMessages.size() != 0) {
			return false;
		}

		return true;
	}
}
