package model.point;


import model.column.IColumn;

/**
* Decrit un Point (ou donnee, ou ligne) dans un DataSet.
* Exemple : une instance d'iris ou d'un passager.
*/
public interface IPoint {
    /**
    * Retourne la valeur de ce point pour la colonne en parametre.
    *
    * Note, on aurait pu utiliser une interface generique (parametree avec
    * un type), mais cela complique significativement d'autres parties
    * du code.
    */
    public Object getValue(IColumn col);

    /**
    * Retourne la valeur de ce point normalisee pour la colonne en parametre.
    *
    * La normalisation se fait avec le <i>normaliseur</i> de la colonne.
    * Si la colonne n'est pas normalisable, le comportement n'est pas defini.
    */
    public double getNormalizedValue(IColumn col);

    public IClassificationValue getClassification();

    public void setClassification(IClassificationValue classificationValue);
}
