package model.point;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassengerTest {
    Passenger passenger;

    @BeforeEach
    public void setup() {
        passenger = new Passenger(1, 2, "Name", "male", 18, 0, 0, "label ticket", 10, "cabin", "P", 0);
    }

    @Test
    public void should_get_passenger_attributes() {
        assertEquals(1, passenger.getPassengerID());
        assertEquals(2, passenger.getPassengerClass());
        assertEquals("Name", passenger.getPassengerName());
        assertEquals(PassengerSex.MALE, passenger.getPassengerSex());
        assertEquals(18, passenger.getPassengerAge());
        assertEquals(0, passenger.getPassengerNumberOfParentsAndChildrenAboard());
        assertEquals(0, passenger.getPassengerNumberOfSiblingAndSpousesAboard());
        assertEquals("label ticket", passenger.getPassengerTicketLabel());
        assertEquals(10, passenger.getPassengerTicketPrice());
        assertEquals("cabin", passenger.getPassengerCabin());
        assertEquals("P", passenger.getPassengerPortOfEmbarkation());
        assertEquals(PassengerHasSurvivedOrNot.NO, passenger.getClassification());
    }

    @Test
    public void should_get_passenger_to_string(){
        assertEquals("{ passengerID='1', passengerClass='2', passengerName='Name', passengerSex='MALE', passengerAge='18.0', passengerNumberOfSiblingAndSpousesAboard='0', passengerNumberOfParentsAndChildrenAboard='0', passengerTicketLabel='label ticket', passengerTicketPrice='10.0', passengerCabin='cabin', passengerPortOfEmbarkation='P', classification='NON'}", passenger.toString());
    }

    @Test
    public void should_get_passenger_sex_french_word(){
        assertEquals("Homme", PassengerSex.MALE.getFrenchWord());
        assertEquals("Femme", PassengerSex.FEMALE.getFrenchWord());
        assertEquals("Inconnu", PassengerSex.UNKNOWN.getFrenchWord());
    }

    @Test
    public void should_get_string_result_passenger_classification(){
        assertEquals("NON", PassengerHasSurvivedOrNot.NO.toString());
        assertEquals("OUI", PassengerHasSurvivedOrNot.YES.toString());
        assertEquals("UNKNOW", PassengerHasSurvivedOrNot.UNKNOWN.toString());

    }
}
