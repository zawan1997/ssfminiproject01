package vttp2022.ssfminiproject01.ssfproj.Repositories;

//import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

//import vttp2022.ssfminiproject01.ssfproj.Models.Location;


@Repository
public class MainRepo {

    @Autowired
    @Qualifier("redislab")
    private RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

    //saving the Location UUID as the key 
    public void saveLocation(String locationUuid, String payload) {

        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        valueOp.set(locationUuid.toString().toLowerCase(), payload);
    }

    public String getLocation(String locationUuid) {
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        String value = valueOp.get(locationUuid.toString().toLowerCase());
        if (null == value) {
            return null;
        }
        return value;
    }
    public void saveUserLocationMap(String userid, String locationUuid, String payload) {

        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        // if this userid already in redis then get the locationId and append the new ID and save back.
        String value = valueOp.get(userid.toString().toLowerCase());
        if(value == null)
            valueOp.set(userid.toString().toLowerCase(), locationUuid.toString().toLowerCase());
        else
        {
            value = value + "," + locationUuid.toString().toLowerCase();
            valueOp.set(userid.toString().toLowerCase(), value);
        }

        saveLocation(locationUuid, payload);
    }

    public String getUserLocationMap(String userid) {

        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        String value = valueOp.get(userid.toString().toLowerCase());
        return value;
    }

    public boolean saveUser(String userID, String password) {
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        // Checking if user is already registered. If yes, then dont need to save just
        // get
        System.out.println("Check if user is already registered" + valueOp.get(userID));
        if (valueOp.get(userID) == null) {
            valueOp.set(userID.toLowerCase(), password);
            // Checking if password is being stored successfully
            System.out.println("Password being stored" + password);
            return true;
        } else
            return false;
    }

    public void saveLoggedInUser(String userID) {
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        valueOp.set(userID.toLowerCase() + "is_loggedin", "1");
        System.out.println("Token is set for user " + userID);
    }

    // Checking if user is valid to "unlock" saving items function"
    // If valid, grant the session token
    public boolean isValidUser(String userID, String password) {
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        System.out.println(valueOp.get(userID.toLowerCase()));
        String passwordStored = valueOp.get(userID.toLowerCase());
        System.out.println("passwordstored" + passwordStored);
        if (password.equals(passwordStored)) {
            saveLoggedInUser(userID);
            return true;
        }
        return false;
    }

    public boolean logoutUser(String userID) {
        boolean isLogout = false;
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        if (isLoggedInUser(userID)) {
            String actualResult = valueOp.getAndDelete(userID.toLowerCase() + "is_loggedin");
            System.out.println("user logged out " + userID);
            System.out.println("actualResult " + actualResult);
            isLogout = true;

        } else {
            System.out.println("No need to logout as user is not logged in");
            isLogout = true;
        }
        return isLogout;

    }

    public boolean isLoggedInUser(String userID) {
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        
        String actualResult = valueOp.get(userID.toLowerCase()+"is_loggedin");
        String isPresent = "1";
        System.out.println("Is User Logged In "+actualResult);
       if(isPresent.equals(actualResult))
       {

            System.out.println( "User is currently logged In");
            return true;
        }
        System.out.println( "User is currently NOT logged In");
        return false;
    }

}
