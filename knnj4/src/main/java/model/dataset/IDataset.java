package model.dataset;

import java.util.List;

import model.point.IPoint;

public interface IDataset extends Iterable<IPoint> {
    //IDataset : l'ensemble des données d'un objet --> ex : ensemble des pokemons
    
    /**
     * Le nom du DataSet ex: Titanic, Iris, Pokemon, ...
     */
    public String getTitle();

    /**
     * Nombre de lignes (ou donnees ou points) dans le DataSet
     */
    public int getNbLines();

    /**
     * stocke dans le DataSet la collection de donnees passee en parametre
     */
    public void setLines(List<IPoint> lines);

    /**
     * Ajoute une donnee dans le DataSet
     */
    public void addLine(IPoint element);

    /**
     * Ajoute une collection de donnees dans le DataSet
     */
    void addAllLine(List<IPoint> element);

    public void clear();
}
