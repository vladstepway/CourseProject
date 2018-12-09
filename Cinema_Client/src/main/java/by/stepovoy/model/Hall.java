package by.stepovoy.model;

import java.io.Serializable;

public class Hall implements IKey, Serializable {

    protected int ID;
    private String type;
    private String name;
    private String floor;
    private String description;
    private String managerPhone;
    private int capacity;


    @Override
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hall)) return false;

        Hall that = (Hall) o;

        if (getID() != that.getID()) return false;
        if (getCapacity() != that.getCapacity()) return false;
        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getFloor() != null ? !getFloor().equals(that.getFloor()) : that.getFloor() != null) return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        return getManagerPhone() != null ? getManagerPhone().equals(that.getManagerPhone()) : that.getManagerPhone() == null;
    }

    @Override
    public int hashCode() {
        int result = getID();
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getFloor() != null ? getFloor().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getManagerPhone() != null ? getManagerPhone().hashCode() : 0);
        result = 31 * result + getCapacity();
        return result;
    }

    @Override
    public String toString() {
        return "Hall{" +
                "ID=" + ID +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", floor='" + floor + '\'' +
                ", description='" + description + '\'' +
                ", managerPhone='" + managerPhone + '\'' +
                ", capacity=" + capacity +
                "}\n";
    }
}