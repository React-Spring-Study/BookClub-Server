package traveler.bookclub.auth.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import traveler.bookclub.auth.exception.AuthErrorCode;
import traveler.bookclub.auth.exception.AuthException;
import traveler.bookclub.common.response.BasicResponse;
import traveler.bookclub.common.response.ErrorEntity;
import traveler.bookclub.common.response.ResponseUtil;

import java.io.IOException;

@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (AuthException e) {
            sendError(response, e.getErrorCode(), e.getMessage());
        }

    }

    public void sendError(HttpServletResponse response, AuthErrorCode code, String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        BasicResponse<Object> basicResponse = ResponseUtil.error(
                new ErrorEntity(code.toString(), message, null)
        );
        try{
            response.setCharacterEncoding("utf-8");
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(basicResponse));
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
