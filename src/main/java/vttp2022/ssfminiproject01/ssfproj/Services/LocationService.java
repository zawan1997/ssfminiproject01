package vttp2022.ssfminiproject01.ssfproj.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import vttp2022.ssfminiproject01.ssfproj.Models.Location;

@Service
public class LocationService {

    public static final String URL = "https://tih-api.stb.gov.sg/content/v1/attractions/search";

    @Value("${API_KEY}")
    private String key;

    public List<Location> getLocation(String location) {
       
        return null;
        
    }
    
}
