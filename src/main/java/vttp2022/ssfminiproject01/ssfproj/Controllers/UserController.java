package vttp2022.ssfminiproject01.ssfproj.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.ssfminiproject01.ssfproj.Services.UserService;

@Controller
@RequestMapping(path={"/user"})
public class UserController {

    @Autowired
    private UserService uSvc;

    @PostMapping("/register")
    public String createUser(Model model, @RequestBody String userData) {
        boolean res = uSvc.createUser(userData);
        if(res){
            return "UserResponseRegister";
        } else {
            return "UserResponseRegisterFail";
        }
    }

    @PostMapping("/login")
    public String loginUser(Model model, @RequestBody String userData) {
        boolean isValid = uSvc.login(userData);

        if(isValid) {
            return "UserResponseSuccess";
        }
        return "UserResponseFailure";
    }
    
    @GetMapping("/logout")
    public String logoutUser(Model model, @RequestParam String userID) {
        boolean isValid = uSvc.logout(userID);
        
        if(isValid)
            return "UserLogoutSuccess";
        return "UserLogoutFailure";
    }
}
