package model.normalizer;

import model.dataset.IDataset;

public interface IValueNormalizer {
    /**
     * Retourne la valeur en parametre normalisee (entre 0 et 1).
     */
    public double normalize(Object value);
    /**
     * De-normalise la valeur en parametre (qui est entre 0 et 1)
     * Retourne la « vraie » valeur correspondante pour la colonne
     * associee au normaliseur
     */
    public Object denormalize(double value);
    
}
