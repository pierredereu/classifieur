package model.point;

public enum PassengerSex {
    MALE("male", "Homme"),
    FEMALE("female", "Femme"),
    UNKNOWN("", "Inconnu");

    private String csvString;
    private String frenchWord;

    private PassengerSex(String csvString, String frenchWord){
        this.csvString = csvString;
        this.frenchWord = frenchWord;
    }

    public String getFrenchWord(){
        return this.frenchWord;
    }

    public String getCsvString(){
        return this.csvString;
    }
}
