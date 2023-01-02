package controller;

import model.column.IColumn;
import model.point.IClassificationValue;
import model.point.IPoint;
import model.point.IrisVariety;

public class IrisNull implements IPoint {

    public String getMessage() {
        return "Erreur dans l'ajout du point. Veuillez entrer des nombres entiers ou à virgules.";
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
        return IrisVariety.UNKNOWN;
    }

    @Override
    public void setClassification(IClassificationValue classificationValue) {
    }

}
