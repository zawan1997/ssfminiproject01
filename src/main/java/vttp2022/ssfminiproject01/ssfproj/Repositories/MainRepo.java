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

    // saving the Location UUID as the key and information as payload like name,
    // uuid etc
    public void saveLocation(String locationUuid, String payload) {

        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        valueOp.set(locationUuid.toString().toLowerCase(), payload);
    }

    // Pass location id and get the detail of location as Json
    public String getLocation(String locationUuid) {
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        String value = valueOp.get(locationUuid.toString().toLowerCase());
        if (null == value) {
            return null;
        }
        return value;
    }

    // For given user we will save the mapping as userid and locations with common
    // seprated and prefix "loc"
    public void saveUserLocationMap(String userid, String locationUuid, String payload) {

        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        // if this userid already in redis then take the locationId and save back.
        String locationKey = "loc";
        // Because we dont want to overrride UserID we just replace with loC
        // Calling userID will just bring password back
        String userKey = userid.toString().toLowerCase() + locationKey;
        String value = valueOp.get(userKey);
        if (value == null)
            valueOp.set(userKey, locationUuid.toString().toLowerCase());
        else {
            value = value + "," + locationUuid.toString().toLowerCase();
            valueOp.set(userKey, value);
        }

        saveLocation(locationUuid, payload);
    }

    // To retrieve all common separated location id for given user.
    public String getUserLocationMap(String userid) {

        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        String locationKey = "loc"; // as we have userid as key already in redis because we store used and password,
                                    // so to save lcoation we will append loc to userid
        String userKey = userid.toString().toLowerCase() + locationKey;
        String value = valueOp.get(userKey);
        return value;
    }

    // To register a user in system with userid and password
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

    // Checking if user is valid to "unlock" saving items function"
    public boolean isValidUser(String userID, String password) {
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        System.out.println(valueOp.get(userID.toLowerCase()));
        String passwordStored = valueOp.get(userID.toLowerCase());
        System.out.println("passwordstored" + passwordStored);
        if (password.equals(passwordStored)) {
            return true;
        }
        return false;
    }

}
