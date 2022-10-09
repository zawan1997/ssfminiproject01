package vttp2022.ssfminiproject01.ssfproj.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp2022.ssfminiproject01.ssfproj.Models.Location;
import vttp2022.ssfminiproject01.ssfproj.Services.LocationService;

@RestController
@RequestMapping(path = "/listing", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocationRestController {

    @Autowired
    private LocationService locationSvc;

    @GetMapping(path = "{userid}")
    public ResponseEntity<String> getRestLocation(@PathVariable String userid) {
        List<Location> list = locationSvc.getLocationPerUser(userid);

        if (list.isEmpty()) {
            JsonObject err = Json.createObjectBuilder()
                    .add("error", "No Locations saved for user %s".formatted(userid))
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(err.toString());
        }
        //for loop for Json Array
         Location loc = new Location();
         JsonArray arr ;
         JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (int i=0; i<list.size(); i++){
           loc = new Location();
           loc = list.get(i);      
         JsonObject uuids = Json.createObjectBuilder()
         //.add("userid", userid)
         .add("uuid "+i, loc.toJson())
         .build();
         arrBuilder.add(uuids);
        }
        arr= arrBuilder.build();
        

        return ResponseEntity.ok(arr.toString());

    }

}
