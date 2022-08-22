package vttp2022.ssfminiproject01.ssfproj.Models;

import jakarta.json.JsonObject;

public class Location {

    private String companyDisplayName;
    private String body;
    private String primaryContactNo;

    public String getCompanyDisplayName() {
        return companyDisplayName;
    }
    public void setCompanyDisplayName(String companyDisplayName) {
        this.companyDisplayName = companyDisplayName;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getPrimaryContactNo() {
        return primaryContactNo;
    }
    public void setPrimaryContactNo(String primaryContactNo) {
        this.primaryContactNo = primaryContactNo;
    }
    public static Location create(JsonObject jo) {
        Location loc = new Location();
        loc.setCompanyDisplayName(jo.getString("main"));
        loc.setBody(jo.getString("description"));
        loc.setPrimaryContactNo(jo.getString("icon"));
        return loc;

    }
}
