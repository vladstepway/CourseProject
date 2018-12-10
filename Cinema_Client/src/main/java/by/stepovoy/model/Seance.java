package by.stepovoy.model;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Date;

public class Seance implements IKey, Serializable {

    private int ID;
    private int hallID;
    private int filmID;
    private Time seanceTime;
    private Date seanceDate;
    private double ticketCost;
    private int ticketsLeft;


    public Seance() {
    }

    public Seance(int ID) {
        this.ID = ID;
    }

    @Override
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getHallID() {
        return hallID;
    }

    public void setHallID(int hallID) {
        this.hallID = hallID;
    }

    public int getFilmID() {
        return filmID;
    }

    public void setFilmID(int filmID) {
        this.filmID = filmID;
    }

    public Time getSeanceTime() {
        return seanceTime;
    }

    public void setSeanceTime(Time seanceTime) {
        this.seanceTime = seanceTime;
    }

    public Date getSeanceDate() {
        return seanceDate;
    }

    public void setSeanceDate(Date seanceDate) {
        this.seanceDate = seanceDate;
    }

    public double getTicketCost() {
        return ticketCost;
    }

    public void setTicketCost(double ticketCost) {
        this.ticketCost = ticketCost;
    }

    public int getTicketsLeft() {
        return ticketsLeft;
    }

    public void setTicketsLeft(int ticketsLeft) {
        this.ticketsLeft = ticketsLeft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seance)) return false;

        Seance seance = (Seance) o;

        if (getID() != seance.getID()) return false;
        if (getHallID() != seance.getHallID()) return false;
        if (getFilmID() != seance.getFilmID()) return false;
        if (Double.compare(seance.getTicketCost(), getTicketCost()) != 0) return false;
        if (getTicketsLeft() != seance.getTicketsLeft()) return false;
        if (getSeanceTime() != null ? !getSeanceTime().equals(seance.getSeanceTime()) : seance.getSeanceTime() != null)
            return false;
        return getSeanceDate() != null ? getSeanceDate().equals(seance.getSeanceDate()) : seance.getSeanceDate() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getID();
        result = 31 * result + getHallID();
        result = 31 * result + getFilmID();
        result = 31 * result + (getSeanceTime() != null ? getSeanceTime().hashCode() : 0);
        result = 31 * result + (getSeanceDate() != null ? getSeanceDate().hashCode() : 0);
        temp = Double.doubleToLongBits(getTicketCost());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getTicketsLeft();
        return result;
    }

    @Override
    public String toString() {
        return "Seance{" +
                "ID=" + ID +
                ", hallID=" + hallID +
                ", filmID=" + filmID +
                ", seanceTime=" + seanceTime +
                ", seanceDate=" + seanceDate +
                ", ticketCost=" + ticketCost +
                ", ticketsLeft=" + ticketsLeft +
                "}\n";
    }
}
