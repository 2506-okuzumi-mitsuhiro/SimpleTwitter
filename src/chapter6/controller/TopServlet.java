package chapter6.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.beans.User;
import chapter6.beans.UserComment;
import chapter6.beans.UserMessage;
import chapter6.logging.InitApplication;
import chapter6.service.CommentService;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/index.jsp" })
public class TopServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public TopServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		boolean isShowMessageForm = false;
		User user = (User) request.getSession().getAttribute("loginUser");
		if (user != null) {
			isShowMessageForm = true;
		}

		// 実践課題 その②修正ヵ所
		// 表示させたいユーザのIDをMessageService().selectに渡すよう修正
		String userId = request.getParameter("user_id");

		// WEB開発基礎課題（つぶやきの絞り込み）
		String inputStartDate = request.getParameter("start");
		String inputEndDated = request.getParameter("end");

		List<UserMessage> messages = new MessageService().select(userId, inputStartDate, inputEndDated);
		request.setAttribute("messages", messages);

		// WEB開発基礎課題（つぶやきの返信）
		List<UserComment> comments = new CommentService().select();
		request.setAttribute("comments", comments);

		request.setAttribute("isShowMessageForm", isShowMessageForm);
		request.getRequestDispatcher("/top.jsp").forward(request, response);
	}
}
