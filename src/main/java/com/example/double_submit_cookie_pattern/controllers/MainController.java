package com.example.double_submit_cookie_pattern.controllers;

import com.example.double_submit_cookie_pattern.models.LoginDataModel;
import com.example.double_submit_cookie_pattern.models.UpdateModel;
import com.example.double_submit_cookie_pattern.models.UserDataModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    private Processes procObj = new Processes();
    private UserDataModel dataModel = new UserDataModel();

    public MainController() {
    }

    @GetMapping("/")
    public ModelAndView handleRequest() {

        return new ModelAndView("login");

    }

    @PostMapping("/login")
    public ModelAndView handleLoginRequest(@ModelAttribute("loginForm") LoginDataModel userData, HttpServletResponse servletResponse) {

        String sessionId;
        String csrfToken;

        ModelAndView modelAndView;
        boolean validity;

        validity = procObj.validateUserCredentials(userData);

        if (validity) {
            csrfToken = procObj.generateRandomValue();
            sessionId = procObj.generateRandomValue();

            dataModel.setUsername(userData.getUserName());
            dataModel.setPassword(userData.getPassword());
            dataModel.setSessionId(sessionId);

            //insert data to HashSet
            Credentials.getInstance().addCredentials(dataModel);

            //generate new session cookie
            Cookie sessionCookie = new Cookie("SESSIONID", sessionId);
            sessionCookie.setMaxAge(60 * 60);
            sessionCookie.setHttpOnly(true);
            sessionCookie.setSecure(false);

            //generate new CSRF cookie cookie
            Cookie csrfCookie = new Cookie("CSRF", csrfToken);
            csrfCookie.setMaxAge(60 * 60);
//          csrfCookie.setHttpOnly(true); cannot read cookie if httponly flag is true
            csrfCookie.setSecure(false);

            servletResponse.addCookie(sessionCookie);
            servletResponse.addCookie(csrfCookie);

            modelAndView = new ModelAndView("infoUpdatePage.html");
            modelAndView.addObject("welcomeText", "Welcome " + userData.getUserName());
        } else {
            modelAndView = new ModelAndView("login.html");
            modelAndView.addObject("errorMessage", "Invalid Credentials");
        }

        return modelAndView;
    }

    @PostMapping("/update")
    public ModelAndView updateDetails(@ModelAttribute("updateForm") UpdateModel updateData, HttpServletRequest servletRequest) {

        String cSRFTokenBody = updateData.getCsrf();
        String cSRFTokenHeader = null;
        String receivedSessionId = null;
        ModelAndView modelAndView = null;

        if (servletRequest.getCookies() != null) {

            //get cookie details
            for (Cookie cookie : servletRequest.getCookies()) {

                System.out.println(cookie.getName());

                if(cookie.getName().equals("CSRF")){
                    cSRFTokenHeader = cookie.getValue();
                }

                if (cookie.getName().equals("SESSIONID")) {
                    receivedSessionId = cookie.getValue();
                }
            }

            if(receivedSessionId.equals(Credentials.getInstance().getSessionId(receivedSessionId)) && cSRFTokenBody.equals(cSRFTokenHeader)){
                modelAndView = new ModelAndView("infoUpdatePage");
                modelAndView.addObject("welcomeText", "Welcome " + Credentials.getInstance().getUserName(receivedSessionId));
                modelAndView.addObject("successMSG", "User Updated successfully");
            }

            if (modelAndView == null){
                modelAndView = new ModelAndView("infoUpdatePage");
                modelAndView.addObject("errorMSG", "Update Failed. Invalid User");
            }
        }

        return modelAndView;
    }

    @PostMapping("/logout")
    public ModelAndView logout(HttpServletRequest servletRequest) {

        if (servletRequest.getCookies() != null) {

            for (Cookie cookie : servletRequest.getCookies()) {
                if (cookie.getName().equals("SESSIONID")) {
                    Credentials.getInstance().removeCredentials(cookie.getValue());
                }
            }
        }

        return new ModelAndView("login.html");

    }

}
