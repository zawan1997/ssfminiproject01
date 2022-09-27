package vttp2022.ssfminiproject01.ssfproj.Controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import vttp2022.ssfminiproject01.ssfproj.Models.UserCreateRequest;
import vttp2022.ssfminiproject01.ssfproj.Models.UserLoginRequest;
import vttp2022.ssfminiproject01.ssfproj.Services.UserService;

@Controller
@RequestMapping(path = { "/user" })
public class UserController {

    @Autowired
    private UserService uSvc;

    @PostMapping(value = "/register", consumes = "application/x-www-form-urlencoded", produces = "text/html")
    public String createUser(@RequestBody MultiValueMap<String, String> form, Model model) {
        UserCreateRequest uCr = new UserCreateRequest();
        uCr.setUserID(form.getFirst("userID"));
        uCr.setPassword(form.getFirst("password"));
        System.out.println("Account details " + uCr);
        boolean res = uSvc.createUser(uCr.toJson().toString());
        System.out.println("Created " + res);
        if (res) {
            return "login";
        } else {
            return "redirect:/register.html";
        }
    }

    // if login success, redirect to search page with saved locations hyperlink. If
    // fail, redirect to login page again
    @PostMapping("/login")
    public String loginUser(Model model, @RequestBody MultiValueMap<String, String> form, HttpSession session) {
        UserLoginRequest uLc = new UserLoginRequest();
        uLc.setUserID(form.getFirst("userID"));
        uLc.setPassword(form.getFirst("password"));
        boolean isValid = uSvc.login(uLc.toJson().toString());

        if (isValid) {
            session.setAttribute("userID", uLc.getUserID());
            return "searchwithuser";
        }
        return "redirect:/login.html";
    }

    @GetMapping("/logout")
    // Just need to invalidate the session number to logout
    public String logoutUser(Model model, HttpSession session) {
        String userID = null;
        if (session.getAttribute("userID") != null) {
            userID = session.getAttribute("userID").toString();
            System.out.println("Current user " + userID);
            session.invalidate();
            return "logoutpage";
        } else
            return "redirect:/index.html";
    }
}
