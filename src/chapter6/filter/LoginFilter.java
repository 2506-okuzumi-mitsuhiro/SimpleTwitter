package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chapter6.beans.User;

// WEB開発基礎課題（ログインフィルター）
@WebFilter(urlPatterns = { "/setting", "/edit" })
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest  = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession();
		User user = (User) session.getAttribute("loginUser");

		if(user != null) {
			chain.doFilter(request, response);
		}else {
			List<String> errorMessages = new ArrayList<String>();
			errorMessages.add("ログインしてください");
			session.setAttribute("errorMessages", errorMessages);
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.sendRedirect("./login");

			return;
		}
	}

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}
}
