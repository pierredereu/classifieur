package model.factory;

import model.input.PassengerBrut;
import model.point.Passenger;

public class PassengerFactory implements IFactory<Passenger, PassengerBrut>{
    private static PassengerFactory instance = null;

    private PassengerFactory() {
    }

    public static PassengerFactory getInstance() {
        if ( instance == null ) {
            instance = new PassengerFactory();
        }
        return instance;
    }

    @Override
    public Passenger createPoint(PassengerBrut passengerBrut) {
        int passengerID = passengerBrut.getPassengerID();
        int hasSurvived = passengerBrut.hasSurvived();
        int passengerClass = passengerBrut.getPassengerClass();
        String passengerSex = passengerBrut.getPassengerSex();
        String passengerName = passengerBrut.getPassengerName();
        double passengerAge = passengerBrut.getPassengerAge();
        int passengerNumberOfSiblingAndSpousesAboard = passengerBrut.getPassengerNumberOfSiblingAndSpousesAboard();
        int passengerNumberOfParentsAndChildrenAboard = passengerBrut.getPassengerNumberOfParentsAndChildrenAboard();
        String passengerTicketLabel = passengerBrut.getPassengerTicketLabel();
        double passengerTicketPrice = passengerBrut.getPassengerTicketPrice();
        String passengerCabin = passengerBrut.getPassengerCabin();
        String passengerPortOfEmbarkation = passengerBrut.getPassengerPortOfEmbarkation();
        return new Passenger(passengerID, passengerClass, passengerName, passengerSex, passengerAge, passengerNumberOfSiblingAndSpousesAboard, passengerNumberOfParentsAndChildrenAboard, passengerTicketLabel, passengerTicketPrice, passengerCabin, passengerPortOfEmbarkation, hasSurvived);
    }
}
