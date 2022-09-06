package vttp2022.ssfminiproject01.ssfproj.Models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class UserCreateRequest {
    private String userID;
    private String password;

   

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    //JSON to model
    //creating a JSON object with the following contents
    //creating as a java object becuase java cannot read JSON directly
    public static UserCreateRequest create(JsonObject jo) {
        UserCreateRequest w = new UserCreateRequest();
        w.setUserID(jo.getString("userID"));
        w.setPassword(jo.getString("password"));
        return w;

    }
    //serialisation
    //forming it back to JSON after reading it in the JSON READER
    public JsonObject toJson() {
        return Json.createObjectBuilder()
        .add("userID", userID)
        .add("password", password)
        .build();
    }
}
