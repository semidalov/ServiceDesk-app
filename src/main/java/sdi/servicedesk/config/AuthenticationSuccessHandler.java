package sdi.servicedesk.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import sdi.servicedesk.dao.EquipmentDAO;
import sdi.servicedesk.models.User;
import sdi.servicedesk.security.AccountDetails;
import sdi.servicedesk.services.UserService;
import sdi.servicedesk.utils.VisitedIPsHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

@Component()
public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    private static final org.apache.log4j.Logger LOGGER = Logger.getLogger(EquipmentDAO.class);
    private final UserService userService;
    private final VisitedIPsHandler visitedIPsHandler;

    @Autowired
    public AuthenticationSuccessHandler(UserService userService, VisitedIPsHandler visitedIPsHandler) {
        this.userService = userService;
        this.visitedIPsHandler = visitedIPsHandler;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            LOGGER.info("User: " + getAccount().getUsername() + " logged at " + LocalDateTime.now());
            userService.setUserLastLogin(getAccount());
            visitedIPsHandler.handleIpAddress(request.getRemoteAddr());
            response.sendRedirect("/tasks");
        }
    }

    private User getAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();
        return accountDetails.getAccount();
    }

}