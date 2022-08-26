package vttp2022.ssfminiproject01.ssfproj.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.ssfminiproject01.ssfproj.Models.Location;
import vttp2022.ssfminiproject01.ssfproj.Services.LocationService;

@Controller
public class LocationController {

    //after searching location, user will select choice. checkbox will determine what else to show 
    @Autowired
    private LocationService lSv;

    @GetMapping
    public String getRequestedLocation(Model model,
    @RequestParam (name="keyword") String keyword){
    List<Location> list = lSv.getLocation(keyword);
    model.addAttribute("keyword", keyword);
    model.addAttribute("list", list);
    return "locationdetails";
    }
}
