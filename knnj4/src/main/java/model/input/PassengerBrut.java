package model.input;

import com.opencsv.bean.CsvBindByName;

public class PassengerBrut implements IPointBrut {
    @CsvBindByName(column = "PassengerId")
    protected int passengerID;
    @CsvBindByName(column = "Survived")
    protected int hasSurvived;
    @CsvBindByName(column = "Pclass")
    protected int passengerClass;
    @CsvBindByName(column = "Name")
    protected String passengerName;
    @CsvBindByName(column = "Sex")
    protected String passengerSex;
    @CsvBindByName(column = "Age")
    protected double passengerAge;
    @CsvBindByName(column = "SibSp")
    protected int passengerNumberOfSiblingAndSpousesAboard;
    @CsvBindByName(column = "Parch")
    protected int passengerNumberOfParentsAndChildrenAboard;
    @CsvBindByName(column = "Ticket")
    protected String passengerTicketLabel;
    @CsvBindByName(column = "Fare")
    protected double passengerTicketPrice;
    @CsvBindByName(column = "Cabin")
    protected String passengerCabin;
    @CsvBindByName(column = "Embarked")
    protected String passengerPortOfEmbarkation;

    public int getPassengerID() {
        return this.passengerID;
    }

    public int hasSurvived() {
        return this.hasSurvived;
    }

    public int getPassengerClass() {
        return this.passengerClass;
    }

    public String getPassengerName() {
        return this.passengerName;
    }

    public String getPassengerSex() {
        return this.passengerSex;
    }

    public double getPassengerAge() {
        return this.passengerAge;
    }

    public int getPassengerNumberOfSiblingAndSpousesAboard() {
        return this.passengerNumberOfSiblingAndSpousesAboard;
    }

    public int getPassengerNumberOfParentsAndChildrenAboard() {
        return this.passengerNumberOfParentsAndChildrenAboard;
    }

    public String getPassengerTicketLabel() {
        return this.passengerTicketLabel;
    }

    public double getPassengerTicketPrice() {
        return this.passengerTicketPrice;
    }

    public String getPassengerCabin() {
        return this.passengerCabin;
    }

    public String getPassengerPortOfEmbarkation() {
        return this.passengerPortOfEmbarkation;
    }

}
