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

    // All other API contents will load only when selected but not save
    // only thing to be saved is location
    // key value pair will remain (location,contents)

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
            System.out.println("payload" + payload);

            mainRepo.save(location, payload);
        } else {
            payload = opt.get();
        }

        // Wanted properties are in different parts of the Json File
        Reader strReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(strReader);
        JsonObject results = jsonReader.readObject();
        JsonArray data = results.getJsonArray("Data");
        // Attempting to get all things that are directly in Data
        List<Location> list = new LinkedList<>();
        for (int i = 0; i < data.size(); i++) {
            JsonObject jo = data.getJsonObject(i);
            list.add(Location.create(jo));
        }
        // getting items in reviews
        JsonArray reviews = data.getJsonArray(2);
        for (int i = 0; i < reviews.size(); i++) {
            JsonObject jo2 = reviews.getJsonObject(i);
            list.add(Location.create(jo2));
        }

        // getting items in businesshour
        JsonArray opnclose = data.getJsonArray(11);
        for (int i = 0; i < opnclose.size(); i++) {
            JsonObject jo3 = reviews.getJsonObject(i);
            list.add(Location.create(jo3));
        }
        //getting items in contact
        JsonObject contact = data.getJsonObject(3);
        JsonObject jo4 = contact.getJsonObject("contact");
        list.add(Location.create(jo4));

        return list;
    }

}
