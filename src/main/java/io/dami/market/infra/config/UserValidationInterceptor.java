package io.dami.market.infra.config;

import io.dami.market.application.user.UserService;
import io.dami.market.domain.user.User;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // userId를 요청 파라미터에서 추출
        // TODO 차후 인증이 필요한 API의 공통 처리를 위해 파라미터가 아닌 헤더로 통일할 것
        String userIdParam = request.getParameter("userId");

        if (userIdParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("userId가 요청에 포함되어 있지 않습니다.");
            return false; // 요청 진행 중단
        }

        try {
            Long userId = Long.parseLong(userIdParam);

            // 유저 검증 (존재 여부 확인)
            User user = userService.getUser(userId);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("해당 userId로 유저를 찾을 수 없습니다.");
                return false; // 요청 진행 중단
            }

            // 유저 검증 성공: 컨트롤러로 진행
            return true;
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("userId 형식이 올바르지 않습니다.");
            return false; // 요청 진행 중단
        }
    }
}
