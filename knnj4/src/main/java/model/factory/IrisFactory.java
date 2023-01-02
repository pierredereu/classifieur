package model.factory;

import model.input.IrisBrut;
import model.point.Iris;

public class IrisFactory implements IFactory<Iris, IrisBrut>{

    private static IrisFactory instance = null;

    private IrisFactory() {
    }

    public static IrisFactory getInstance() {
        if ( instance == null ) {
            instance = new IrisFactory();
        }
        return instance;
    }

    @Override
    public Iris createPoint(IrisBrut irisBrut) {
        double sepalLength = irisBrut.getSepalLength();
        double sepalWidth = irisBrut.getSepalWidth();
        double petalLength = irisBrut.getPetalLength();
        double petalWidth = irisBrut.getPetalWidth();
        String classification = irisBrut.getVariety();
        return new Iris(sepalLength,sepalWidth,petalLength,petalWidth,classification);
    }
}
