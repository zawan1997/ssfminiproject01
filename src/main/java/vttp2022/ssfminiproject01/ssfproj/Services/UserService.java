package vttp2022.ssfminiproject01.ssfproj.Services;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp2022.ssfminiproject01.ssfproj.Models.UserCreateRequest;
import vttp2022.ssfminiproject01.ssfproj.Models.UserLoginRequest;
import vttp2022.ssfminiproject01.ssfproj.Repositories.MainRepo;

@Service
public class UserService {
    
    @Autowired
    private MainRepo mRepo;

    //Account creation
    public boolean createUser(String userData) {
        JsonObject jo = Json.createReader(new StringReader(userData)).readObject();
        UserCreateRequest req = UserCreateRequest.create(jo);
        //userData vs String username, Sitnr password

        //For user to request, must be registered 
        boolean successfulRegistration = mRepo.saveUser(req.getUserID(), req.getPassword());
        if(successfulRegistration) {
            System.out.println("Successful Registration");
        }
        else {
            System.out.println("User already exists");
        }
        return successfulRegistration;
    }

    //Logging in
    public boolean login(String userData) {

        JsonObject jo = Json.createReader(new StringReader(userData)).readObject();
        UserLoginRequest req = UserLoginRequest.create(jo);

        //checking if the user exists to allow login
        boolean isValid = mRepo.isValidUser(req.getUserID(), req.getPassword());
        System.out.println("User ID" + req.getUserID());
        System.out.println("Password" + req.getPassword());

        System.out.println("User Valid?" + isValid);
        return isValid;
    }
    public String getUserID(String userData) {

        JsonObject jo = Json.createReader(new StringReader(userData)).readObject();
        UserLoginRequest req = UserLoginRequest.create(jo);

        return req.getUserID();
    }

    public boolean logout(String userID) {
        boolean loggingOut = mRepo.logoutUser(userID);
        return loggingOut;
    }
}
