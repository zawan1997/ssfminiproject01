package vttp2022.ssfminiproject01.ssfproj.Services;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    public List<Location> getLocationWithName(String location, String userID) {
        if(userID.length()>0)
        {
            String userName = userID;
            if(!mainRepo.isLoggedInUser(userName)) {
                System.out.println("User is not User" +userName);
                return null;
            }
            else {
                System.out.println("User is valid" + userName);
                return getLocation(location);
            }
        }
        else {
            return getLocation(location);
        }
    }


    public List<Location> getLocation(String location) {

        Optional<String> opt = mainRepo.get(location);
        String payload;

        if (opt.isEmpty()) {
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
            //System.out.println("payload" + payload);

            mainRepo.save(location, payload);
        } else {
            payload = opt.get();
        }

        // Wanted properties are in different parts of the Json File
        Reader strReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(strReader);
        JsonObject results = jsonReader.readObject();
        JsonArray data = results.getJsonArray("data");

        // Attempting to get all things that are directly in Data
        List<Location> list = new LinkedList<>();

        for(int i=0;i<data.size();i++)
        {
          
        Location loc = new Location();
        loc.setName(data.getJsonObject(i).getString("name"));
        loc.setBody(data.getJsonObject(i).getString("body"));
        loc.setPrimaryContactNo(data.getJsonObject(i).getJsonObject("contact").getString("primaryContactNo"));
        loc.setAuthorName(data.getJsonObject(i).getJsonArray("reviews").getJsonObject(0).getString("authorName"));
        //loc.setOpenTime(data.getJsonObject(i).getJsonArray("businessHour").getJsonObject(0).getString("openTime"));
        //loc.setCloseTime(data.getJsonObject(i).getJsonArray("businessHour").getJsonObject(0).getString("closeTime"));
        loc.setText(data.getJsonObject(i).getJsonArray("reviews").getJsonObject(0).getString("text"));
        loc.setTime(data.getJsonObject(i).getJsonArray("reviews").getJsonObject(0).getString("time"));
        loc.setRating(data.getJsonObject(i).getJsonArray("reviews").getJsonObject(0).getInt("rating"));
        //loc.setLibraryUuid(data.getJsonObject(i).getJsonArray("images").getJsonObject(0).getString("libraryUuid"));


        list.add(loc);

        //JsonObject locJsonObject = loc.toJson();

        }
       

        return list;
    }

}
