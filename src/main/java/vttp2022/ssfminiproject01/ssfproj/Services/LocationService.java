package vttp2022.ssfminiproject01.ssfproj.Services;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
//import java.util.Optional;
import java.util.Map;
//import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.ssfminiproject01.ssfproj.Models.Location;
import vttp2022.ssfminiproject01.ssfproj.Repositories.MainRepo;

@Service
public class LocationService {

    public static final String URL = "https://tih-api.stb.gov.sg/content/v1/attractions/search?language=en";

    @Value("${API_KEY}")
    private String key;

    @Autowired
    private MainRepo mainRepo;

    private Map<String, String> locationMap = null;

    //Unused
    public List<Location> saveLocationtoList(String location, String userID) {
        if (userID.length() > 0) {
            String userName = userID;
            if (!mainRepo.isLoggedInUser(userName)) {
                System.out.println("User is not User" + userName);
                return null;
            } else {
                System.out.println("User is valid" + userName);
                return getLocation(location);
            }
        } else {
            return getLocation(location);
        }
    }

    //Checking if user is logged in before approving the save
    public boolean saveLocationForUser(String userID, String locationUuid) {

        if (userID.length() > 0) {
            if (!mainRepo.isLoggedInUser(userID)) {
                System.out.println("User is not User" + userID);
                return false;
            } else {
                System.out.println("User is valid " + userID);
                String payload = locationMap.get(locationUuid);
                mainRepo.saveUserLocationMap(userID, locationUuid, payload);
                return true;
            }
        } else {
            return false;
        }
    }

    // Getting saved locations per user from Redis
    public List<Location> getLocationPerUser(String userID) {
        List<Location> list = new LinkedList<>();

        String locationIDListStr = mainRepo.getUserLocationMap(userID);
        System.out.println("location for userid  "+userID+"   "+locationIDListStr);
        if(locationIDListStr == null || locationIDListStr.isEmpty() || locationIDListStr.isBlank())
        {
            return list;
        }
        String[] locationList = locationIDListStr.split("[,]", 0);

        // We need to split locationID list by comma
        for (String locationUuid : locationList) {
            String payload = mainRepo.getLocation(locationUuid);
            Location loc = new Location();
            Reader strReader = new StringReader(payload);
            JsonReader jsonReader = Json.createReader(strReader);
            JsonObject results = jsonReader.readObject();
            loc.setName(results.getString("name"));
            loc.setBody(results.getString("body"));
            loc.setPrimaryContactNo(results.getJsonObject("contact").getString("primaryContactNo"));
            loc.setAuthorName(results.getJsonArray("reviews").getJsonObject(0).getString("authorName"));
            loc.setOpenTime(results.getJsonArray("businessHour").getJsonObject(0).getString("openTime"));
            loc.setCloseTime(results.getJsonArray("businessHour").getJsonObject(0).getString("closeTime"));
            loc.setText(results.getJsonArray("reviews").getJsonObject(0).getString("text"));
            loc.setTime(results.getJsonArray("reviews").getJsonObject(0).getString("time"));
            loc.setRating(results.getJsonArray("reviews").getJsonObject(0).getInt("rating"));
            loc.setLibraryUuid(results.getJsonArray("images").getJsonObject(0).getString("libraryUuid"));
            list.add(loc);
        }

        return list;
    }

    public List<Location> getLocation(String location) {

        String payload;
        locationMap = new HashMap<>();

        System.out.println("Getting fresh from Location API");
        String url = UriComponentsBuilder.fromUriString(URL)
                .queryParam("keyword", location)
                .queryParam("apikey", key)
                .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url).build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);

        if (resp.getStatusCodeValue() != 200) {
            System.err.println("Error status code not 200");
            return Collections.emptyList();
        }

        payload = resp.getBody();

        // Wanted properties are in different parts of the Json File
        Reader strReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(strReader);
        JsonObject results = jsonReader.readObject();
        JsonArray data = results.getJsonArray("data");

        // Attempting to get all things that are directly in Data
        List<Location> list = new LinkedList<>();
        System.out.println("There are " + data.size() + "  records");
        for (int i = 0; i < data.size(); i++) {

            Location loc = new Location();
            loc.setName(getProperText(data.getJsonObject(i).getString("name")));
            System.out.println("Name " + loc.getName());
            loc.setUuid(getProperText(data.getJsonObject(i).getString("uuid")));
            System.out.println("UUID " + loc.getUuid());
            loc.setBody(cleanup((getProperText(data.getJsonObject(i).getString("body")))));
            System.out.println("body " + loc.getBody().substring(0, 1));
            loc.setPrimaryContactNo(
                    getProperText(data.getJsonObject(i).getJsonObject("contact").getString("primaryContactNo")));
            System.out.println("primaryContactNo " + loc.getPrimaryContactNo());
            if (hasData(data.getJsonObject(i).getJsonArray("reviews")))
                loc.setAuthorName(getProperText(
                        data.getJsonObject(i).getJsonArray("reviews").getJsonObject(0).getString("authorName")));
            else
                loc.setAuthorName("NA");
            System.out.println("authorName " + loc.getAuthorName());
            if (hasData(data.getJsonObject(i).getJsonArray("businessHour"))) {
                loc.setOpenTime(getProperText(
                        data.getJsonObject(i).getJsonArray("businessHour").getJsonObject(0).getString("openTime")));
                loc.setCloseTime(getProperText(
                        data.getJsonObject(i).getJsonArray("businessHour").getJsonObject(0).getString("closeTime")));
            } else {
                loc.setOpenTime("NA");
                loc.setCloseTime("NA");
            }
            System.out.println("openTime " + loc.getOpenTime());
            System.out.println("closeTime " + loc.getCloseTime());
            if (hasData(data.getJsonObject(i).getJsonArray("reviews"))) {
                loc.setText(getProperText(
                        data.getJsonObject(i).getJsonArray("reviews").getJsonObject(0).getString("text")));
                loc.setTime(getProperText(
                        data.getJsonObject(i).getJsonArray("reviews").getJsonObject(0).getString("time")));
                loc.setRating(data.getJsonObject(i).getJsonArray("reviews").getJsonObject(0).getInt("rating"));
            } else {
                loc.setText("NA");
                loc.setTime("NA");
                loc.setRating(0);
            }
            System.out.println("text " + loc.getText().substring(0, 1));
            System.out.println("Time " + loc.getTime());
            System.out.println("Rating " + loc.getRating());

            if (hasData(data.getJsonObject(i).getJsonArray("images"))) {
                loc.setLibraryUuid(getProperText(
                        data.getJsonObject(i).getJsonArray("images").getJsonObject(0).getString("libraryUuid")));
            } else {
                loc.setLibraryUuid("NA");
            }
            System.out.println("libraryUuid " + loc.getLibraryUuid());
            list.add(loc);
            String locationUuid = loc.getUuid();
            String payLoadPerLocation = data.getJsonObject(i).toString();
            locationMap.put(locationUuid, payLoadPerLocation);
        }

        return list;
    }

    // Replacing all empty Strings with NA
    // Attempt to not return anything at all if 1 is NA
    private String getProperText(String text) {
        String returnValue = "NA";

        if (!(text == null || text.isEmpty() || text.isBlank())) {
            returnValue = text;
        }

        return returnValue;
    }

    // Checking if the array exists
    private boolean hasData(JsonArray arr) {
        return arr.size() > 0;
    }

    // Method to clean up all HTML traces from the result
    private String cleanup(String s) {
        String clean = s.replaceAll("\\<.*?>", "").replace("&nbsp;", "");
        return clean;
    }

    //REST Controller method
   
}
