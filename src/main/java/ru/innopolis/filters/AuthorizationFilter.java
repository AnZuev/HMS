package ru.innopolis.filters;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.HttpStatus;
import ru.innopolis.constants.AuthorizationConstant;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Создано: Денис
 * Дата:  29.10.2016
 * Описание: Фильт предназначен для проверки аутентифицировался ли пользователь или нет.
 */
public class AuthorizationFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);
        if (session != null){
            Boolean attribute = (Boolean) session.getAttribute(AuthorizationConstant.AUTHORIZATION_KEY);
            if (BooleanUtils.isTrue(attribute)){
                chain.doFilter(request, response);
            }else {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        }
    }
}
