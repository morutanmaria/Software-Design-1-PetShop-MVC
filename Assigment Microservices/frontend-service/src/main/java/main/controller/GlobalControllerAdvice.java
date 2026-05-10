package main.controller;

import main.dto.LoggedUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("loggedUser")
    public LoggedUser addUserToModel(HttpSession session) {
        return (LoggedUser) session.getAttribute("loggedUser");
    }
}
