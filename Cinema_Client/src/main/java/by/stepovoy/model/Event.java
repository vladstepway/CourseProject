//package by.stepovoy.model;
//
//import by.stepovoy.IKey;
//
//import java.io.Serializable;
//
//public abstract class Event implements IKey, Serializable {
//
//    protected int ID;
//    protected int duration;
//    protected String description;
//    protected String genre;
//    protected String country;
//    protected int ageLimit;
//
//    @Override
//    public int getID() {
//        return ID;
//    }
//
//    public void setID(int id) {
//        this.ID = id;
//    }
//
//    public int getDuration() {
//        return duration;
//    }
//
//    public void setDuration(int duration) {
//        this.duration = duration;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getGenre() {
//        return genre;
//    }
//
//    public void setGenre(String genre) {
//        this.genre = genre;
//    }
//
//    public String getCountry() {
//        return country;
//    }
//
//    public void setCountry(String country) {
//        this.country = country;
//    }
//
//    public int getAgeLimit() {
//        return ageLimit;
//    }
//
//    public void setAgeLimit(int ageLimit) {
//        this.ageLimit = ageLimit;
//    }
//
//    public abstract String getName();
//}
//
