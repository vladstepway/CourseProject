package by.stepovoy.model;

import by.stepovoy.utils.IKey;

import java.io.Serializable;

public class Film implements IKey, Serializable {

    private int ID;
    private int duration;
    private String description;
    private String genre;
    private String country;
    private int yearProduction;
    private int ageLimit;
    private String name;
    private String director;
    private boolean show3D;

    @Override
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public boolean isShow3D() {
        return show3D;
    }

    public void setShow3D(boolean show3D) {
        this.show3D = show3D;
    }

    public int getYearProduction() {
        return yearProduction;
    }

    public void setYearProduction(int yearProduction) {
        this.yearProduction = yearProduction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;

        Film film = (Film) o;

        if (getID() != film.getID()) return false;
        if (getDuration() != film.getDuration()) return false;
        if (getYearProduction() != film.getYearProduction()) return false;
        if (getAgeLimit() != film.getAgeLimit()) return false;
        if (isShow3D() != film.isShow3D()) return false;
        if (getDescription() != null ? !getDescription().equals(film.getDescription()) : film.getDescription() != null)
            return false;
        if (getGenre() != null ? !getGenre().equals(film.getGenre()) : film.getGenre() != null) return false;
        if (getCountry() != null ? !getCountry().equals(film.getCountry()) : film.getCountry() != null) return false;
        if (getName() != null ? !getName().equals(film.getName()) : film.getName() != null) return false;
        return getDirector() != null ? getDirector().equals(film.getDirector()) : film.getDirector() == null;
    }

    @Override
    public int hashCode() {
        int result = getID();
        result = 31 * result + getDuration();
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getGenre() != null ? getGenre().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        result = 31 * result + getYearProduction();
        result = 31 * result + getAgeLimit();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getDirector() != null ? getDirector().hashCode() : 0);
        result = 31 * result + (isShow3D() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Film{" +
                "ID=" + ID +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", country='" + country + '\'' +
                ", yearProduction=" + yearProduction +
                ", ageLimit=" + ageLimit +
                ", name='" + name + '\'' +
                ", director='" + director + '\'' +
                ", show3D=" + show3D +
                '}';
    }
}
