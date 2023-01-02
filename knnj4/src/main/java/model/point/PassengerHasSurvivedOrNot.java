package model.point;

public enum PassengerHasSurvivedOrNot implements IClassificationValue{
    NO(0,"NON"),
    YES(1,"OUI"),
    UNKNOWN("UNKNOW");

    private int binaryResult;
    private String stringResult;

    private PassengerHasSurvivedOrNot(int n , String s){
        binaryResult = n;
        stringResult = s;
    }

    private PassengerHasSurvivedOrNot(String s){
        stringResult = s;
    }

    public int getBinaryResult(){
        return binaryResult;
    }

    @Override
    public String toString(){
        return stringResult;
    }

}
