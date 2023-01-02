package controller;

import model.column.IColumn;
import model.point.IClassificationValue;
import model.point.IPoint;
import model.point.PassengerHasSurvivedOrNot;

public class PassengerNull implements IPoint {

    public String getMessage() {
        return "Erreur dans l'ajout du point.";
    }

    @Override
    public Object getValue(IColumn col) {
        return -1;
    }

    @Override
    public double getNormalizedValue(IColumn col) {
        return -1;
    }

    @Override
    public IClassificationValue getClassification() {
        return PassengerHasSurvivedOrNot.UNKNOWN;
    }

    @Override
    public void setClassification(IClassificationValue classificationValue) {
    }

}
