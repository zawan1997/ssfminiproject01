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

    // saving the Location UUID as the key and information as payload 
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

    // Mapping userid as key for both location and payload
    public void saveUserLocationMap(String userid, String locationUuid, String payload) {

        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        // if this userid already in redis then take the locationId and save back.
        String locationKey = "loc";
        // Add loc so password doesnt get overritten
        String userKey = userid.toString().toLowerCase() + locationKey;
        String value = valueOp.get(userKey);
        //If got no value then store normally then next one will be comma seperated
        if (value == null)
            valueOp.set(userKey, locationUuid.toString().toLowerCase());
            //seperating each uuid with a comma so they dont get mixed
        else {
            value = value + "," + locationUuid.toString().toLowerCase();
            valueOp.set(userKey, value);
        }
        
        saveLocation(locationUuid, payload);
    }

    // To retrieve all location id for user.
    public String getUserLocationMap(String userid) {

        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        // getting location from userloc
        String locationKey = "loc"; 
        String userKey = userid.toString().toLowerCase() + locationKey;
        String value = valueOp.get(userKey);
        return value;
    }

    // Registering user
    public boolean saveUser(String userID, String password) {
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        // Checking if user is already registered
        System.out.println("Check if user is already registered" + valueOp.get(userID));
        if (valueOp.get(userID) == null) {
            valueOp.set(userID.toLowerCase(), password);
            // Checking if password is being stored successfully
            System.out.println("Password being stored" + password);
            return true;
        } else
            return false;
    }

    // Checking if user is valid to unlock saving items function"
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
