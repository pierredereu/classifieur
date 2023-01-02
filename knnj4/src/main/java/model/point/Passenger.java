package model.point;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import model.column.IColumn;

public class Passenger implements IPoint {
	protected int passengerID;
	protected int passengerClass;
	protected String passengerName;
	protected PassengerSex passengerSex;
	protected double passengerAge;
	protected int passengerNumberOfSiblingAndSpousesAboard;
	protected int passengerNumberOfParentsAndChildrenAboard;
	protected String passengerTicketLabel;
	protected double passengerTicketPrice;
	protected String passengerCabin;
	protected String passengerPortOfEmbarkation;
	protected PassengerHasSurvivedOrNot classification;

	public Passenger(int passengerID, int passengerClass, String passengerName, String passengerSex,
			double passengerAge, int passengerNumberOfSiblingAndSpousesAboard,
			int passengerNumberOfParentsAndChildrenAboard, String passengerTicketLabel, double passengerTicketPrice,
			String passengerCabin, String passengerPortOfEmbarkation, double hasSurvived) {
		super();
		this.passengerID = passengerID;
		this.passengerClass = passengerClass;
		this.passengerName = passengerName;
		this.passengerAge = passengerAge;
		this.passengerNumberOfSiblingAndSpousesAboard = passengerNumberOfSiblingAndSpousesAboard;
		this.passengerNumberOfParentsAndChildrenAboard = passengerNumberOfParentsAndChildrenAboard;
		this.passengerTicketLabel = passengerTicketLabel;
		this.passengerTicketPrice = passengerTicketPrice;
		this.passengerCabin = passengerCabin;
		this.passengerPortOfEmbarkation = passengerPortOfEmbarkation;

		setPassengerSex(passengerSex);
		setPassengerClassification(hasSurvived);
	}

	public void setPassengerSex(String passengerSex){
		this.passengerSex = PassengerSex.UNKNOWN;
		for(PassengerSex sex : List.of(PassengerSex.MALE, PassengerSex.FEMALE)) {
            if(passengerSex.equals(sex.getCsvString())){
                this.passengerSex = sex;
            }
        }
	}

	public void setPassengerClassification(double hasSurvived){
		this.classification = PassengerHasSurvivedOrNot.UNKNOWN;
		for(PassengerHasSurvivedOrNot survived : List.of(PassengerHasSurvivedOrNot.YES, PassengerHasSurvivedOrNot.NO)) {
            if(hasSurvived == survived.getBinaryResult()){
                this.classification = survived;
            }
        }
	}


	public Passenger() {
	}

	@Override
	public Object getValue(IColumn col) {
		for (Map.Entry<IPoint, Object> values : col.getColumnValues().entrySet()) {
			if (this.equals(values.getKey()))
				return values.getValue();
		}
		return null;
	}

	@Override
	public double getNormalizedValue(IColumn col) {
		return col.getNormalizedValue(this);
	}

	public int getPassengerID() {
		return passengerID;
	}

	public int getPassengerClass() {
		return passengerClass;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public PassengerSex getPassengerSex() {
		return passengerSex;
	}

	public double getPassengerAge() {
		return passengerAge;
	}

	public int getPassengerNumberOfSiblingAndSpousesAboard() {
		return passengerNumberOfSiblingAndSpousesAboard;
	}

	public int getPassengerNumberOfParentsAndChildrenAboard() {
		return passengerNumberOfParentsAndChildrenAboard;
	}

	public String getPassengerTicketLabel() {
		return passengerTicketLabel;
	}

	public double getPassengerTicketPrice() {
		return passengerTicketPrice;
	}

	public String getPassengerCabin() {
		return passengerCabin;
	}

	public String getPassengerPortOfEmbarkation() {
		return passengerPortOfEmbarkation;
	}

	@Override
	public PassengerHasSurvivedOrNot getClassification() {
		return this.classification;
	}


	@Override
	public String toString() {
		return "{" +
			" passengerID='" + getPassengerID() + "'" +
			", passengerClass='" + getPassengerClass() + "'" +
			", passengerName='" + getPassengerName() + "'" +
			", passengerSex='" + getPassengerSex() + "'" +
			", passengerAge='" + getPassengerAge() + "'" +
			", passengerNumberOfSiblingAndSpousesAboard='" + getPassengerNumberOfSiblingAndSpousesAboard() + "'" +
			", passengerNumberOfParentsAndChildrenAboard='" + getPassengerNumberOfParentsAndChildrenAboard() + "'" +
			", passengerTicketLabel='" + getPassengerTicketLabel() + "'" +
			", passengerTicketPrice='" + getPassengerTicketPrice() + "'" +
			", passengerCabin='" + getPassengerCabin() + "'" +
			", passengerPortOfEmbarkation='" + getPassengerPortOfEmbarkation() + "'" +
			", classification='" + getClassification() + "'" +
			"}";
	}



	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Passenger)) {
			return false;
		}
		Passenger passenger = (Passenger) o;
		return passengerID == passenger.passengerID && passengerClass == passenger.passengerClass && Objects.equals(passengerName, passenger.passengerName) && Objects.equals(passengerSex, passenger.passengerSex) && passengerAge == passenger.passengerAge && passengerNumberOfSiblingAndSpousesAboard == passenger.passengerNumberOfSiblingAndSpousesAboard && passengerNumberOfParentsAndChildrenAboard == passenger.passengerNumberOfParentsAndChildrenAboard && Objects.equals(passengerTicketLabel, passenger.passengerTicketLabel) && passengerTicketPrice == passenger.passengerTicketPrice && Objects.equals(passengerCabin, passenger.passengerCabin) && Objects.equals(passengerPortOfEmbarkation, passenger.passengerPortOfEmbarkation) && Objects.equals(classification, passenger.classification);
	}

	@Override
	public int hashCode() {
		return Objects.hash(passengerID, passengerClass, passengerName, passengerSex, passengerAge, passengerNumberOfSiblingAndSpousesAboard, passengerNumberOfParentsAndChildrenAboard, passengerTicketLabel, passengerTicketPrice, passengerCabin, passengerPortOfEmbarkation, classification);
	}

	@Override
	public void setClassification(IClassificationValue classificationValue) {
		this.classification = (PassengerHasSurvivedOrNot) classificationValue;
	}
}