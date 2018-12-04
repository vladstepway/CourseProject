package by.stepovoy.model;

import by.stepovoy.IKey;

import java.io.Serializable;

public class Ticket implements IKey, Serializable {

    private int ID;
    private int seanceID;
    private int userID;
    private int seatNumber;
    private int amountTickets;
    private double cost;

    @Override
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getSeanceID() {
        return seanceID;
    }

    public void setSeanceID(int seanceID) {
        this.seanceID = seanceID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getAmountTickets() {
        return amountTickets;
    }

    public void setAmountTickets(int amountTickets) {
        this.amountTickets = amountTickets;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;

        Ticket ticket = (Ticket) o;

        if (getID() != ticket.getID()) return false;
        if (getSeanceID() != ticket.getSeanceID()) return false;
        if (getUserID() != ticket.getUserID()) return false;
        if (getSeatNumber() != ticket.getSeatNumber()) return false;
        if (getAmountTickets() != ticket.getAmountTickets()) return false;
        return Double.compare(ticket.getCost(), getCost()) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getID();
        result = 31 * result + getSeanceID();
        result = 31 * result + getUserID();
        result = 31 * result + getSeatNumber();
        result = 31 * result + getAmountTickets();
        temp = Double.doubleToLongBits(getCost());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ID=" + ID +
                ", seanceID=" + seanceID +
                ", userID=" + userID +
                ", seatNumber=" + seatNumber +
                ", amountTickets=" + amountTickets +
                ", cost=" + cost +
                '}';
    }
}