package sdi.servicedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sdi.servicedesk.models.User;
import sdi.servicedesk.security.AccountDetails;
import sdi.servicedesk.services.AccountService;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/account")
@Controller
public class AccountController {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountController(AccountService accountService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    @PatchMapping("/reset_pass/{id}")
    public String resetPassword(@PathVariable("id") int id) {
        accountService.resetPasswordByUserId(id);
        return "redirect:/users/get_user/" + id;
    }

    @GetMapping("/change_pass")
    public String changePass() {
        return "/auth/change_pass";
    }


    @PatchMapping("/change_pass")
    public String applyPass(HttpServletRequest httpServletRequest, Model model) {

        if (!passwordEncoder.matches(httpServletRequest.getParameter("old_password"), getPassword())) {
            model.addAttribute("message", "Неверный пароль");
            return "/auth/change_pass";
        }

        accountService.updatePassword(httpServletRequest.getParameter("password"), getUser());
        return "redirect:/tasks";
    }

    private String getPassword() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();
        return accountDetails.getAccount().getPassword();
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();
        return accountDetails.getAccount();
    }
}
