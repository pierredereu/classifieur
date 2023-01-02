package model;

import java.io.IOException;

public class RobustnessDemo {

    public static void runIrisPlatform() throws IllegalStateException, IOException{
        IrisPlatformModel model = new IrisPlatformModel();
        System.out.println("IRIS \n\n");
        model.importTrainingDataset("data/iris/iris.csv");
        for(int i = 1 ; i < 50 ; i++){
            System.out.println("La robustesse pour k = " + i + " est de " + model.getRobustness().getRobustnessValue(i));
        }
        
        System.out.println("\nLa robustesse maximale est de " + model.getRobustness().getMaxRobustnessValue() + " pour k = " + model.getRobustness().getBestK());
    }

    public static void runPassengerPlatform() throws IllegalStateException, IOException{
        PassengerPlatformModel model = new PassengerPlatformModel();
        model.importTrainingDataset("data/titanic/titanic.csv");
        System.out.println("TITANIC \n\n");
        for(int i = 1 ; i < 50 ; i++){
            System.out.println("La robustesse pour k = " + i + " est de " + model.getRobustness().getRobustnessValue(i));
        }
        
        System.out.println("\nLa robustesse maximale est de " + model.getRobustness().getMaxRobustnessValue() + " pour k = " + model.getRobustness().getBestK());
    }

    public static void main(String[] args) {
        try {
            runIrisPlatform();
            runPassengerPlatform();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
