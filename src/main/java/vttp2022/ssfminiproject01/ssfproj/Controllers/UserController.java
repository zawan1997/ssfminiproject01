package vttp2022.ssfminiproject01.ssfproj.Controllers;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.ssfminiproject01.ssfproj.Models.UserCreateRequest;
import vttp2022.ssfminiproject01.ssfproj.Models.UserLoginRequest;
import vttp2022.ssfminiproject01.ssfproj.Services.UserService;

@Controller
@RequestMapping(path={"/user"})
public class UserController {

    @Autowired
    private UserService uSvc;

    //@PostMapping("/register")
    // @RequestMapping(value="/register",
    //             method=RequestMethod.POST,
    //             consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    // public String createUser(Model model, MultiValueMap <String, String>  userData) {
    //     String userID = userData.get("userID").toString();
    //     String password = userData.get("password").toString();
    //     UserCreateRequest uRc = new UserCreateRequest();
    //     uRc.setUserID(userID);
    //     uRc.setPassword(password);
    //     boolean res= uSvc.createUser(uRc.toJson().toString());
    //     System.out.println("Response create "+res);
    //     if(res){
    //         return "login";
    //     } else {
    //         return "redirect:/register";
    //     }
    // }

    @PostMapping(value ="/register", consumes="application/x-www-form-urlencoded" ,produces ="text/html")
    public String createUser(@RequestBody MultiValueMap<String, String> form, Model model) {
        UserCreateRequest uCr = new UserCreateRequest();
        uCr.setUserID(form.getFirst("userID"));
        uCr.setPassword(form.getFirst("password"));
        System.out.println("Account details " + uCr);
        boolean res = uSvc.createUser(uCr.toJson().toString());
        System.out.println("Created "+ res);
        if(res) {
            return "login";
        } else {
            return "redirect:/register.html";
        }
    }

    //if login success, redirect to search page with saved locations hyperlink. If fail, redirect to login page again
    @PostMapping("/login")
    public String loginUser(Model model, @RequestBody MultiValueMap<String, String> form,HttpSession session) {
        UserLoginRequest uLc = new UserLoginRequest();
        uLc.setUserID(form.getFirst("userID"));
        uLc.setPassword(form.getFirst("password"));
        boolean isValid = uSvc.login(uLc.toJson().toString());

        if(isValid) {
            session.setAttribute("userID", uLc.getUserID());
            return "searchwithuser";
        }
        return "redirect:/login.html";
    }
    
    @GetMapping("/logout")
    public String logoutUser(Model model, HttpSession session) {
        String userID = null;
        if(session.getAttribute("userID")!=null)
        {
            userID = session.getAttribute("userID").toString();
            System.out.println("Current user "+userID);
            boolean isValid = uSvc.logout(userID);
            session.invalidate();
            if(isValid)
                return "logoutpage";
            else
                return "UserLogoutFailure";    
        }
        else
            return "redirect:/index.html";
    }
}
