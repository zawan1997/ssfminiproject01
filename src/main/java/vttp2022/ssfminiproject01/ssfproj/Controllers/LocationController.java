package vttp2022.ssfminiproject01.ssfproj.Controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.ssfminiproject01.ssfproj.Models.Location;
import vttp2022.ssfminiproject01.ssfproj.Services.LocationService;

@Controller
@RequestMapping(path = { "places" })
public class LocationController {

    // after searching location, user will select choice. checkbox will determine
    // what else to show
    @Autowired
    private LocationService lSv;

    @GetMapping
    @RequestMapping("/searchLocation")
    public String getRequestedLocation(Model model,
            @RequestParam(name = "keyword") String keyword) {
        List<Location> list = lSv.getLocation(keyword);
        model.addAttribute("keyword", keyword);
        model.addAttribute("list", list);
        return "locations";
    }

    @GetMapping
    @RequestMapping("/saveLocPerUser")
    public String saveLocationForLoggedUser(Model model, @RequestParam String uuid, HttpSession session) {
        System.out.println("uuid " + uuid);
        System.out.println("Current User : " + session.getAttribute("userID"));
        if (session.getAttribute("userID") != null) {
            String userID = session.getAttribute("userID").toString();
            lSv.saveLocationForUser(userID, uuid);
            List<Location> list = lSv.getLocationPerUser(userID);
            model.addAttribute("userid", userID);
            model.addAttribute("list", list);
            return "locationsperuser";
        } else {
            System.out.println("Not logged in currently;");
            return "login";
        }

    }

    @GetMapping
    @RequestMapping("/getLocPerUser")
    public String getLocationForUser(Model model, HttpSession session) {
        String userID = null;
        if (session.getAttribute("userID") != null) {
            userID = session.getAttribute("userID").toString();
            List<Location> list = lSv.getLocationPerUser(userID);
            if (list.size() > 0) {
                model.addAttribute("userid", userID);
                model.addAttribute("list", list);
                return "locationsperuser";
            } else {
                System.out.println("No location saved");
                return "nolocation";
            }

        } else {
            return "login";
        }

    }

}
