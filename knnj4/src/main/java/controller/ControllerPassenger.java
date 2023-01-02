package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.PassengerPlatformModel;
import model.distance.DistanceAlgorithm;
import model.point.IPoint;
import model.point.Passenger;
import model.point.PassengerHasSurvivedOrNot;
import model.point.PassengerSex;

public class ControllerPassenger implements Initializable {

    private static final double initialK = 24;

    protected PassengerPlatformModel passengerPlatformModel;

    @FXML
    private Button buttonValiderPassenger;

    @FXML
    private Button buttonClassifierLesDonneesPassenger;

    @FXML
    private Button buttonConstruireLeModelePassenger;

    @FXML
    private Button buttonDonneeAClassifierPassenger;

    @FXML
    private ChoiceBox<String> choiceBoxAbscissePassenger;

    @FXML
    private ChoiceBox<String> choiceBoxDistancePassenger;

    @FXML
    private ChoiceBox<String> choiceBoxOrdonneePassenger;

    @FXML
    private ScatterChart<Number, Number> graphiquePassenger;

    @FXML
    private NumberAxis idAbscissePassenger;

    @FXML
    private HBox idHbox1;

    @FXML
    private Label idMeilleurKPassenger;

    @FXML
    private NumberAxis idOrdonneePassenger;

    @FXML
    private Slider idSliderPassenger;

    @FXML
    private VBox idVBox1;

    @FXML
    private Label labelSliderPassenger;

    @FXML
    private RadioButton radioButtonNNPassenger;

    @FXML
    private RadioButton radioButtonNPassenger;

    @FXML
    private ToggleGroup groupPassenger;

    @FXML
    private TableView<Passenger> tableViewPassenger;

    @FXML
    private TableColumn<Passenger, Double> age;

    @FXML
    private TableColumn<Passenger, String> cabine;

    @FXML
    private TableColumn<Passenger, Integer> classe;

    @FXML
    private TableColumn<Passenger, Integer> id;

    @FXML
    private TableColumn<Passenger, String> nom;

    @FXML
    private TableColumn<Passenger, String> nomTicket;

    @FXML
    private TableColumn<Passenger, Integer> parch;

    @FXML
    private TableColumn<Passenger, String> porteEmbarquement;

    @FXML
    private TableColumn<Passenger, Double> prixTicket;

    @FXML
    private TableColumn<Passenger, PassengerSex> sex;

    @FXML
    private TableColumn<Passenger, Integer> sibsp;

    @FXML
    private TableColumn<Passenger, PassengerHasSurvivedOrNot> survie;

    @FXML
    private Label labelMaxRobustesse;

    @FXML
    private Label labelRobustessePassenger;

    @FXML
    private Button ajouterPassager;

    @FXML
    private TextArea textAreaAge;

    @FXML
    private TextArea textAreaCabine;

    @FXML
    private TextArea textAreaClasse;

    @FXML
    private TextArea textAreaId;

    @FXML
    private TextArea textAreaNom;

    @FXML
    private TextArea textAreaNumeroTicket;

    @FXML
    private TextArea textAreaParch;

    @FXML
    private TextArea textAreaPorte;

    @FXML
    private TextArea textAreaPrixTicket;

    @FXML
    private TextArea textAreaSibsp;

    @FXML
    private ChoiceBox<String> choiceBoxSex;

    @FXML
    private GridPane gridPane;

    @FXML
    private Button resetAllPassenger;

    protected final String FICHIER_CSV_UNIQUEMENT = "Fichier csv uniquement";
    protected final String FILES_CSV = "*.csv";
    protected final String LOCAL_DIR = System.getProperty("user.dir");

    public ControllerPassenger() {
        passengerPlatformModel = new PassengerPlatformModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        survieModelSeries.setName("Survie Model");
        mortModelSeries.setName("Mort Model");
        survieSuspectSeries.setName("Survie Suspect");
        mortSuspectSeries.setName("Mort Suspect");
        unknownPassenger.setName("Unknown Passenger");
        graphiquePassenger.setAnimated(false);

        setupKSlider();
        choicePassengerX();
        choicePassengerY();
        setupBestK();
        setupDistanceNormalizedPassenger();

        setupDistanceAlgorithm();
        choiceSex();
    }

    protected final String ID = "Identifiant";
    protected final String CLASS = "Classe";
    protected final String AGE = "Age";
    protected final String SIBSP = "SIBSP";
    protected final String PARCH = "PARCH";
    protected final String TICKET_PRICE = "Prix du ticket";

    List<String> ListPassengerX = List.of(ID, CLASS, AGE, SIBSP, PARCH, TICKET_PRICE);
    List<String> ListPassengerY = List.of(ID, CLASS, AGE, SIBSP, PARCH, TICKET_PRICE);

    @FXML
    void onActionClassifierLesDonneesPassenger(ActionEvent event) {
        if (!passengerPlatformModel.getSuspectDataset().getPoints().isEmpty()) {
            classifierPassenger();
            clearTableViewSuspect();
            tableViewPassengerSuspect();
        }
    }

    @FXML
    void onActionConstruireLeModelePassenger(ActionEvent event) throws IllegalStateException, IOException {
        FileChooser fc = new FileChooser();
        File initialFile = new File(LOCAL_DIR, "data" + File.separator + "titanic");
        fc.setInitialDirectory(initialFile);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(FICHIER_CSV_UNIQUEMENT, FILES_CSV);
        fc.getExtensionFilters().add(extFilter);
        File selectedFile = fc.showOpenDialog(null);
        String file = "";
        if (selectedFile != null) {
            file = selectedFile.getAbsolutePath();
        } else {
            return;
        }
        passengerPlatformModel.importTrainingDataset(file);
        passengerPlatformModel.findBestK();
        tableViewPassengerTraining();
    }

    public void setupBestK() {
        passengerPlatformModel.getBestK().addListener((subject, data) -> {
            idMeilleurKPassenger.setText(Integer.toString(passengerPlatformModel.getBestK().getValue()));
            labelMaxRobustesse.setText(Double.toString(passengerPlatformModel.getMaxRobustnessValue()));
        });
    }

    @FXML
    void onActionDonneesAClassifierPassenger(ActionEvent event) throws IllegalStateException, IOException {
        FileChooser fc = new FileChooser();
        File initialFile = new File(LOCAL_DIR, "data" + File.separator + "titanic");
        fc.setInitialDirectory(initialFile);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(FICHIER_CSV_UNIQUEMENT, FILES_CSV);
        fc.getExtensionFilters().add(extFilter);
        File selectedFile = fc.showOpenDialog(null);
        String file = "";
        if (selectedFile != null) {
            file = selectedFile.getAbsolutePath();
        } else {
            return;
        }
        passengerPlatformModel.importSuspectDataset(file);
        tableViewPassengerSuspect();
    }

    @FXML
    void onActionValiderPassenger(ActionEvent event) {
        graphiquePassenger.getData().clear();
        graphiqueTrainingDataSetPassenger();
        if (!passengerPlatformModel.getSuspectDataset().getPoints().isEmpty()) {
            graphiqueSuspectDataSetPassenger();
        }
        graphiquePassenger.getData().addAll(survieModelSeries, mortModelSeries, survieSuspectSeries, mortSuspectSeries,
                unknownPassenger);

    }

    XYChart.Series<Number, Number> survieModelSeries = new XYChart.Series<>();;
    XYChart.Series<Number, Number> mortModelSeries = new XYChart.Series<>();;
    XYChart.Series<Number, Number> survieSuspectSeries = new XYChart.Series<>();;
    XYChart.Series<Number, Number> mortSuspectSeries = new XYChart.Series<>();;
    XYChart.Series<Number, Number> unknownPassenger = new XYChart.Series<>();;

    private void setupKSlider() {
        idSliderPassenger.valueProperty().set(initialK);

        idSliderPassenger.valueProperty().addListener((subject, oldValue, newValue) -> {
            passengerPlatformModel.setK(newValue.intValue());
        });
        passengerPlatformModel.getK().addListener((subject, data) -> {
            labelSliderPassenger.setText(Integer.toString(passengerPlatformModel.getK().getValue()));
        });
    }

    private void graphiqueTrainingDataSetPassenger() {
        clearSeriesTrainingDataSetPassenger();
        for (IPoint point : passengerPlatformModel.getTrainingDataset().getPoints()) {
            Passenger passenger = (Passenger) point;
            if (passenger.getClassification() == PassengerHasSurvivedOrNot.YES) {
                survieModelSeries.getData()
                        .add(new XYChart.Data<>((Number) getValueAxisPassenger(getChoicePassengerX(), passenger),
                                (Number) getValueAxisPassenger(getChoicePassengerY(), passenger)));
            } else if (passenger.getClassification() == PassengerHasSurvivedOrNot.NO) {
                mortModelSeries.getData()
                        .add(new XYChart.Data<>((Number) getValueAxisPassenger(getChoicePassengerX(), passenger),
                                (Number) getValueAxisPassenger(getChoicePassengerY(), passenger)));
            } else {
                unknownPassenger.getData()
                        .add(new XYChart.Data<>((Number) getValueAxisPassenger(getChoicePassengerX(), passenger),
                                (Number) getValueAxisPassenger(getChoicePassengerY(), passenger)));
            }
        }
    }

    private void graphiqueSuspectDataSetPassenger() {
        clearSeriesSuspectDataSetPassenger();
        for (IPoint point : passengerPlatformModel.getSuspectDataset().getPoints()) {
            Passenger passenger = (Passenger) point;
            if (passenger.getClassification() == PassengerHasSurvivedOrNot.YES) {
                survieSuspectSeries.getData()
                        .add(new XYChart.Data<>((Number) getValueAxisPassenger(getChoicePassengerX(), passenger),
                                (Number) getValueAxisPassenger(getChoicePassengerY(), passenger)));
            } else if (passenger.getClassification() == PassengerHasSurvivedOrNot.NO) {
                mortSuspectSeries.getData()
                        .add(new XYChart.Data<>((Number) getValueAxisPassenger(getChoicePassengerX(), passenger),
                                (Number) getValueAxisPassenger(getChoicePassengerY(), passenger)));
            } else {
                unknownPassenger.getData()
                        .add(new XYChart.Data<>((Number) getValueAxisPassenger(getChoicePassengerX(), passenger),
                                (Number) getValueAxisPassenger(getChoicePassengerY(), passenger)));
            }
        }
    }

    private void clearSeriesTrainingDataSetPassenger() {
        survieModelSeries.getData().clear();
        mortModelSeries.getData().clear();
        unknownPassenger.getData().clear();
    }

    private void clearSeriesSuspectDataSetPassenger() {
        survieSuspectSeries.getData().clear();
        mortSuspectSeries.getData().clear();
        unknownPassenger.getData().clear();
    }

    private Double getValueAxisPassenger(String str, Passenger passenger) {
        if (str.equals(ID)) {
            return (double) passenger.getPassengerID();
        } else if (str.equals(CLASS)) {
            return (double) passenger.getPassengerClass();
        } else if (str.equals(AGE)) {
            return passenger.getPassengerAge();
        } else if (str.equals(SIBSP)) {
            return (double) passenger.getPassengerNumberOfSiblingAndSpousesAboard();
        } else if (str.equals(PARCH)) {
            return (double) passenger.getPassengerNumberOfParentsAndChildrenAboard();
        }
        return passenger.getPassengerTicketPrice();
    }

    private void choicePassengerX() {
        choiceBoxAbscissePassenger.getItems().addAll(ListPassengerX);
        choiceBoxAbscissePassenger.getSelectionModel().select(2);
    }

    private void choicePassengerY() {
        choiceBoxOrdonneePassenger.getItems().addAll(ListPassengerY);
        choiceBoxOrdonneePassenger.getSelectionModel().select(1);
    }

    private String getChoicePassengerX() {
        String valuePassengerX = choiceBoxAbscissePassenger.getValue();
        return valuePassengerX;
    }

    private String getChoicePassengerY() {
        String valuePassengerY = choiceBoxOrdonneePassenger.getValue();
        return valuePassengerY;
    }

    public void setupDistanceAlgorithm() {
        List<DistanceAlgorithm> distanceAlgorithmList = List.of(DistanceAlgorithm.values());
        choiceBoxDistancePassenger.getItems().addAll(
                distanceAlgorithmList
                        .stream()
                        .map(value -> value.toString())
                        .collect(Collectors.toList()));

        choiceBoxDistancePassenger.getSelectionModel()
                .select(passengerPlatformModel.getClassifier().getDistanceUsed().getAlgorithmUsed().getValue()
                        .toString());

        choiceBoxDistancePassenger.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    passengerPlatformModel.setDistanceAlgorithm(DistanceAlgorithm.valueOf(newValue.toString()));
                    passengerPlatformModel.findBestK();
                });
    }

    public void setupDistanceNormalizedPassenger() {
        radioButtonNPassenger.selectedProperty().set(true);

        radioButtonNPassenger.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == true)
                passengerPlatformModel.setDistanceNormalized(true);
        });

        radioButtonNNPassenger.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == true)
                passengerPlatformModel.setDistanceNormalized(false);
        });
    }

    public void classifierPassenger() {
        passengerPlatformModel.classify();
        passengerPlatformModel.getRobustness();
    }

    public void tableViewPassengerTraining() {
        setCellValue();
        for (IPoint point : passengerPlatformModel.getTrainingDataset().getPoints()) {
            Passenger passenger = (Passenger) point;
            tableViewPassenger.getItems().add(passenger);
        }
    }

    public void tableViewPassengerSuspect() {
        setCellValue();
        for (IPoint point : passengerPlatformModel.getSuspectDataset().getPoints()) {
            Passenger passenger = (Passenger) point;
            tableViewPassenger.getItems().add(passenger);
        }
    }

    public void clearTableViewSuspect() {
        for (IPoint point : passengerPlatformModel.getSuspectDataset().getPoints()) {
            Passenger passenger = (Passenger) point;
            if (tableViewPassenger.getItems().contains(passenger)) {
                tableViewPassenger.getItems().remove(passenger);
            }
        }
    }

    private void setCellValue() {
        id.setCellValueFactory(new PropertyValueFactory<Passenger, Integer>("passengerID"));
        classe.setCellValueFactory(new PropertyValueFactory<Passenger, Integer>("passengerClass"));
        age.setCellValueFactory(new PropertyValueFactory<Passenger, Double>("passengerAge"));
        sex.setCellValueFactory(new PropertyValueFactory<Passenger, PassengerSex>("passengerSex"));
        sibsp.setCellValueFactory(
                new PropertyValueFactory<Passenger, Integer>("passengerNumberOfSiblingAndSpousesAboard"));
        parch.setCellValueFactory(
                new PropertyValueFactory<Passenger, Integer>("passengerNumberOfParentsAndChildrenAboard"));
        nomTicket.setCellValueFactory(new PropertyValueFactory<Passenger, String>("passengerTicketLabel"));
        prixTicket.setCellValueFactory(new PropertyValueFactory<Passenger, Double>("passengerTicketPrice"));
        nom.setCellValueFactory(new PropertyValueFactory<Passenger, String>("passengerName"));
        porteEmbarquement
                .setCellValueFactory(new PropertyValueFactory<Passenger, String>("passengerPortOfEmbarkation"));
        cabine.setCellValueFactory(new PropertyValueFactory<Passenger, String>("passengerCabin"));
        survie.setCellValueFactory(new PropertyValueFactory<Passenger, PassengerHasSurvivedOrNot>("classification"));
    }

    protected final String HOMME = "male";
    protected final String FEMME = "female";
    List<String> ListSex = List.of(HOMME, FEMME);

    private void choiceSex() {
        choiceBoxSex.getItems().addAll(ListSex);
    }

    private String getChoiceSex() {
        String sex = choiceBoxSex.getValue();
        return sex;
    }

    private static boolean isDouble(String s) {
        boolean isValid = true;
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            isValid = false;
        }
        return isValid;
    }

    private static boolean isInteger(String s) {
        boolean isValid = true;
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            isValid = false;
        }
        return isValid;
    }

    @FXML
    void onActionAjouterPassager(ActionEvent event) {
        if (!textAreaId.getText().isEmpty() &&
                !textAreaClasse.getText().isEmpty() &&
                !textAreaNom.getText().isEmpty() &&
                !textAreaAge.getText().isEmpty() &&
                !textAreaSibsp.getText().isEmpty() &&
                !textAreaParch.getText().isEmpty() &&
                !textAreaNumeroTicket.getText().isEmpty() &&
                !textAreaPrixTicket.getText().isEmpty() &&
                !textAreaCabine.getText().isEmpty() &&
                !textAreaPorte.getText().isEmpty() &&
                !choiceBoxSex.getValue().isEmpty() &&
                isInteger(textAreaId.getText()) &&
                isInteger(textAreaClasse.getText()) &&
                isInteger(textAreaSibsp.getText()) &&
                isInteger(textAreaParch.getText()) &&
                isDouble(textAreaAge.getText()) &&
                isDouble(textAreaPrixTicket.getText())) {
            Integer id = Integer.parseInt(textAreaId.getText());
            Integer classe = Integer.parseInt(textAreaClasse.getText());
            String nom = textAreaNom.getText();
            String sex = getChoiceSex();
            Double age = Double.parseDouble(textAreaAge.getText());
            Integer sbisp = Integer.parseInt(textAreaSibsp.getText());
            Integer parch = Integer.parseInt(textAreaParch.getText());
            String numeroTicket = textAreaNumeroTicket.getText();
            Double prixTicket = Double.parseDouble(textAreaPrixTicket.getText());
            String cabine = textAreaCabine.getText();
            String porte = textAreaPorte.getText();
            Passenger passenger = new Passenger(id, classe, nom, sex, age, sbisp, parch, numeroTicket, prixTicket,
                    cabine, porte, 3);
            //System.out.println(passenger.toString());
            passengerPlatformModel.addSuspectPoint(passenger);
            //System.err.println(passengerPlatformModel.getSuspectDataset().getPoints().contains(passenger));
            clearTableViewSuspect();
            tableViewPassengerSuspect();
        } else {
            PassengerNull passengerNull = new PassengerNull();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Le passager n'a pas été ajouté !");
            alert.setContentText(passengerNull.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void onActionResetAllPassenger(ActionEvent event) {
        tableViewPassenger.getItems().clear();
        passengerPlatformModel.getTrainingDataset().clear();
        passengerPlatformModel.getSuspectDataset().clear();
        clearSeriesSuspectDataSetPassenger();
        clearSeriesTrainingDataSetPassenger();
        graphiquePassenger.getData().clear();
        graphiquePassenger.getData().addAll(survieModelSeries, mortModelSeries, survieSuspectSeries, mortSuspectSeries,
                unknownPassenger);
    }

}
