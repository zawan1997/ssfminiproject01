package vttp2022.ssfminiproject01.ssfproj.Models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Location {

    private String name; // inside array Data
    private String body; // inside array Data
    private String primaryContactNo; // inside array Data
    private String openTime; // inside array Data>array businessHour
    private String closeTime; // inside array Data>array businessHour
    private String text; // inside array Data>array reviews
    private int rating; // inside array Data>array reviews
    private String authorName; // inside array Data>array reviews
    private String time; // inside array Data>array reviews
    //private String libraryUuid; //Change to primaryFileMediumUuid//
    private String primaryFileMediumUuid;
  

    private String uuid; // Key to save the location

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPrimaryFileMediumUuid() {
        return primaryFileMediumUuid;
    }

    public void setPrimaryFileMediumUuid(String primaryFileMediumUuid) {
        this.primaryFileMediumUuid = primaryFileMediumUuid;
    }

    // public String getLibraryUuid() {
    //     return libraryUuid;
    // }

    // public void setLibraryUuid(String libraryUuid) {
    //     this.libraryUuid = libraryUuid;
    // }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    // To be able to store information as Json
    public static Location create(JsonObject jo) {
        Location loc = new Location();
        loc.setName(jo.getString("name"));
        loc.setBody(jo.getString("body"));
        loc.setPrimaryContactNo(jo.getString("primaryContactNo"));
        loc.setAuthorName(jo.getString("authorName"));
        loc.setOpenTime(jo.getString("openTime"));
        loc.setCloseTime(jo.getString("closeTime"));
        loc.setText(jo.getString("text"));
        loc.setTime(jo.getString("time"));
        loc.setRating(jo.getInt("rating"));
        //loc.setLibraryUuid(jo.getString("libraryUuid"));
        loc.setPrimaryFileMediumUuid("primaryFileMediumUuid");
        loc.setUuid(jo.getString("uuid"));
        return loc;

    }

    //
    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("name", name)
                .add("body", body)
                .add("primaryContactNo", primaryContactNo)
                .add("authorName", authorName)
                .add("openTime", openTime)
                .add("closeTime", closeTime)
                .add("text", text)
                .add("time", time)
                .add("rating", rating)
                //.add("libraryUuid", libraryUuid)
                .add("uuid", uuid)
                .add("primaryFileMediumUuid", primaryFileMediumUuid)

                .build();
    }
}
