package ru.innopolis.filters;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.HttpStatus;
import ru.innopolis.constants.AuthorizationConstant;
import ru.innopolis.dao.entity.Employee;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Создано: Денис
 * Дата:  10.11.2016
 * Описание: Фильтр проверки прав доступа сотрудника
 */
public class PermissionCheckFilter implements Filter {

    private static final String TYPE_NAME_PARAMETER = "type";
    private Employee.Type type;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);
        boolean isProblem = true;
        if (session != null){
            Employee employee = (Employee) session.getAttribute(AuthorizationConstant.EMPLOYEE_KEY);
            if (employee != null && employee.getType() == type){
                    chain.doFilter(request, response);
                    isProblem = false;
            }
        }

        if(isProblem){
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        String t = filterConfig.getInitParameter(TYPE_NAME_PARAMETER).toUpperCase();
        type = Enum.valueOf(Employee.Type.class, t);
    }
}
