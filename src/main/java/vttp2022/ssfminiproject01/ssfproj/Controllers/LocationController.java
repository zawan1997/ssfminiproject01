package vttp2022.ssfminiproject01.ssfproj.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.ssfminiproject01.ssfproj.Models.Location;
import vttp2022.ssfminiproject01.ssfproj.Services.LocationService;

@Controller
@RequestMapping(path={"places"})
public class LocationController {

    //after searching location, user will select choice. checkbox will determine what else to show 
    @Autowired
    private LocationService lSv;

    @GetMapping
    @RequestMapping("/searchLocation")
    public String getRequestedLocation(Model model,
    @RequestParam (name="keyword") String keyword){
    List<Location> list = lSv.getLocation(keyword);
    model.addAttribute("keyword", keyword);
    model.addAttribute("list", list);
    return "locations";
    }

     @GetMapping
    @RequestMapping("/saveLocPerUser")
    public String gesaveLocationForLoggedUser(Model model, @RequestParam String userID, @RequestParam String locationUuid ) {
        lSv.saveLocationForUser(userID, locationUuid);
        return "savedLocationSuccess";
    }


    @GetMapping
    @RequestMapping("/getLocPerUser")
    public String getLocationForUser(Model model,
    @RequestParam (name="userid") String userid){
    List<Location> list = lSv.getLocationPerUser(userid);
    model.addAttribute("userid", userid);
    model.addAttribute("list", list);
    return "locationsperuser";
    }

    // @GetMapping
    // @RequestMapping("/validSearch")
    // public String getWeatherWithValidName(Model model, @RequestParam String term, @RequestParam String userID) {
    //     List<Location> locations = lSv.getLocationWithName(term, userID);
    //     model.addAttribute("term", term);
    //     model.addAttribute("location", locations);
    //     return "locations";
    // }
}
