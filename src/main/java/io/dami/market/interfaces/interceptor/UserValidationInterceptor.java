package io.dami.market.interfaces.interceptor;

import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class UserValidationInterceptor implements HandlerInterceptor {

  private final UserService userService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String userIdParam = request.getParameter("userId");

    if (userIdParam == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("userId가 요청에 포함되어 있지 않습니다.");
      return false;
    }

    try {
      Long userId = Long.parseLong(userIdParam);

      User user = userService.getUser(userId);

      if (user == null) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("해당 userId로 유저를 찾을 수 없습니다.");
        return false;
      }

      return true;
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("userId 형식이 올바르지 않습니다.");
      return false;
    }
  }
}
