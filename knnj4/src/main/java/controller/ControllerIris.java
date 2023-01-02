package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Axis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import model.IrisPlatformModel;
import model.distance.DistanceAlgorithm;
import model.point.IPoint;
import model.point.Iris;
import model.point.IrisVariety;

public class ControllerIris implements Initializable {

    protected IrisPlatformModel irisPlatformModel;

    protected final int initialK = 4;

    @FXML
    private Button buttonValiderIris;

    @FXML
    private Button buttonClassifierLesDonneesIris;

    @FXML
    private Button buttonConstruireLeModeleIris;

    @FXML
    private Button buttonDonnerAClassifierIris;

    @FXML
    private ChoiceBox<String> choiceBoxAbscisseIris;

    @FXML
    private ChoiceBox<String> choiceBoxDistanceIris;

    @FXML
    private ChoiceBox<String> choiceBoxOrdonneeIris;

    @FXML
    private ScatterChart<Number, Number> graphiqueIris;

    @FXML
    private Axis<Number> idAbscisseIris;

    @FXML
    private Axis<Number> idOrdonneeIris;

    @FXML
    private Slider idSliderIris;

    @FXML
    private Label labelSliderIris;

    @FXML
    private RadioButton radioButtonNIris;

    @FXML
    private RadioButton radioButtonNNIris;

    @FXML
    private ToggleGroup groupIris;

    @FXML
    private Label labelMaxRobustesse;

    @FXML
    private Label labelMeilleurKIris;

    @FXML
    private Label labelRobustesseIris;

    @FXML
    private TableView<Iris> irisTableView;

    @FXML
    private TableColumn<Iris, Double> petalLength;

    @FXML
    private TableColumn<Iris, Double> petalWidth;

    @FXML
    private TableColumn<Iris, Double> sepalLength;

    @FXML
    private TableColumn<Iris, Double> sepalWidth;

    @FXML
    private TableColumn<Iris, IrisVariety> variety;

    @FXML
    private Button buttonAjouterUnPointIris;

    @FXML
    private TextArea textAreaLargeurPetale;

    @FXML
    private TextArea textAreaLargeurSepale;

    @FXML
    private TextArea textAreaLongueurPetale;

    @FXML
    private TextArea textAreaLongueurSepale;

    @FXML
    private Button resetAllIris;

    protected final String FICHIER_CSV_UNIQUEMENT = "Fichier csv uniquement";
    protected final String FILES_CSV = "*.csv";
    protected final String LONGUEUR_PETALE = "Longueur petale";
    protected final String LARGEUR_PETALE = "Largeur petale";
    protected final String LONGUEUR_SEPALE = "Longueur sepale";
    protected final String LARGEUR_SEPALE = "Largeur sepale";
    List<String> ListIrisX = List.of(LONGUEUR_PETALE, LARGEUR_PETALE, LONGUEUR_SEPALE, LARGEUR_SEPALE);
    List<String> ListIrisY = List.of(LONGUEUR_PETALE, LARGEUR_PETALE, LONGUEUR_SEPALE, LARGEUR_SEPALE);
    protected final String LOCAL_DIR = System.getProperty("user.dir");

    public ControllerIris() {
        irisPlatformModel = new IrisPlatformModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setosaModelSeries.setName("Setosa Model");
        versicolorModelSeries.setName("Versicolor Model");
        virginicaModelSeries.setName("Virginica Model");
        setosaSuspectSeries.setName("Setosa Suspect");
        versicolorSuspectSeries.setName("Versicolor Suspect");
        virginicaSuspectSeries.setName("Virginica Suspect");
        unknownIris.setName("Unknown Iris");
        graphiqueIris.setAnimated(false);
        setupKSlider();
        choiceIrisX();
        choiceIrisY();
        setupBestK();
        setupDistanceNormalizedIris();
        setupDistanceAlgorithm();
        updateCurrentRobustnessLabel();
        this.labelRobustesseIris.setText("");
    }

    @FXML
    void onActionClassifierLesDonneesIris(ActionEvent event) {
        if (!irisPlatformModel.getSuspectDataset().getPoints().isEmpty()) {
            classifierIris();
            clearTableViewSuspect();
            tableViewIrisSuspect();
        }
    }

    @FXML
    void onActionConstruireLeModeleIris(ActionEvent event) throws IllegalStateException, IOException {
        FileChooser fc = new FileChooser();
        File initialFile = new File(LOCAL_DIR, "data" + File.separator + "iris");
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
        irisPlatformModel.importTrainingDataset(file);
        irisPlatformModel.findBestK();
        tableViewIrisTraining();
        updateCurrentRobustnessLabel();
    }

    public void updateCurrentRobustnessLabel(){
        irisPlatformModel.updateRobustness();
        labelRobustesseIris.setText(irisPlatformModel.getRobustnessValue(irisPlatformModel.getK().getValue()) + "");
    }

    @FXML
    void onActionDonneesAClassifierIris(ActionEvent event) throws IllegalStateException, IOException {
        FileChooser fc = new FileChooser();
        File initialFile = new File(LOCAL_DIR, "data" + File.separator + "iris");
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
        irisPlatformModel.importSuspectDataset(file);
        tableViewIrisSuspect();
    }

    @FXML
    void onActionValiderIris(ActionEvent event) {
        graphiqueIris.getData().clear();
        graphiqueTrainingDataSetIris();
        if (!irisPlatformModel.getSuspectDataset().getPoints().isEmpty()) {
            graphiqueSuspectDatasetIris();
        }
        graphiqueIris.getData().addAll(setosaModelSeries, versicolorModelSeries, virginicaModelSeries,
                setosaSuspectSeries, versicolorSuspectSeries, virginicaSuspectSeries, unknownIris);
    }

    XYChart.Series<Number, Number> setosaModelSeries = new XYChart.Series<>();;
    XYChart.Series<Number, Number> versicolorModelSeries = new XYChart.Series<>();;
    XYChart.Series<Number, Number> virginicaModelSeries = new XYChart.Series<>();;
    XYChart.Series<Number, Number> setosaSuspectSeries = new XYChart.Series<>();;
    XYChart.Series<Number, Number> versicolorSuspectSeries = new XYChart.Series<>();;
    XYChart.Series<Number, Number> virginicaSuspectSeries = new XYChart.Series<>();;
    XYChart.Series<Number, Number> unknownIris = new XYChart.Series<>();;

    private void setupKSlider() {
        idSliderIris.valueProperty().set(initialK);

        idSliderIris.valueProperty().addListener((subject, oldValue, newValue) -> {
            irisPlatformModel.setK(newValue.intValue());
            updateCurrentRobustnessLabel();
        });
        irisPlatformModel.getK().addListener((subject, data) -> {
            labelSliderIris.setText(Integer.toString(irisPlatformModel.getK().getValue()));
            updateCurrentRobustnessLabel();
        });
    }

    private void graphiqueTrainingDataSetIris() {
        clearSeriesTrainingDataSetIris();
        for (IPoint point : irisPlatformModel.getTrainingDataset().getObservablePoints()) {
            Iris iris = (Iris) point;
            if (iris.getClassification() == IrisVariety.SETOSA) {
                setosaModelSeries.getData().add(new XYChart.Data<>((Number) getValueAxisIris(getChoiceIrisX(), iris),
                        (Number) getValueAxisIris(getChoiceIrisY(), iris)));
            } else if (iris.getClassification() == IrisVariety.VERSICOLOR) {
                versicolorModelSeries.getData()
                        .add(new XYChart.Data<>((Number) getValueAxisIris(getChoiceIrisX(), iris),
                                (Number) getValueAxisIris(getChoiceIrisY(), iris)));
            } else if (iris.getClassification() == IrisVariety.VIRGINICA) {
                virginicaModelSeries.getData().add(new XYChart.Data<>((Number) getValueAxisIris(getChoiceIrisX(), iris),
                        (Number) getValueAxisIris(getChoiceIrisY(), iris)));
            } else {
                unknownIris.getData().add(new XYChart.Data<>((Number) getValueAxisIris(getChoiceIrisX(), iris),
                        (Number) getValueAxisIris(getChoiceIrisY(), iris)));
            }
        }
    }

    private void graphiqueSuspectDatasetIris() {
        clearSeriesSuspectDataSetIris();
        for (IPoint point : irisPlatformModel.getSuspectDataset().getPoints()) {
            Iris iris = (Iris) point;
            if (iris.getClassification() == IrisVariety.SETOSA) {
                setosaSuspectSeries.getData().add(new XYChart.Data<>((Number) getValueAxisIris(getChoiceIrisX(), iris),
                        (Number) getValueAxisIris(getChoiceIrisY(), iris)));
            } else if (iris.getClassification() == IrisVariety.VERSICOLOR) {
                versicolorSuspectSeries.getData()
                        .add(new XYChart.Data<>((Number) getValueAxisIris(getChoiceIrisX(), iris),
                                (Number) getValueAxisIris(getChoiceIrisY(), iris)));
            } else if (iris.getClassification() == IrisVariety.VIRGINICA) {
                virginicaSuspectSeries.getData()
                        .add(new XYChart.Data<>((Number) getValueAxisIris(getChoiceIrisX(), iris),
                                (Number) getValueAxisIris(getChoiceIrisY(), iris)));
            } else {
                unknownIris.getData().add(new XYChart.Data<>((Number) getValueAxisIris(getChoiceIrisX(), iris),
                        (Number) getValueAxisIris(getChoiceIrisY(), iris)));
            }

        }
    }

    private void clearSeriesTrainingDataSetIris() {
        setosaModelSeries.getData().clear();
        versicolorModelSeries.getData().clear();
        virginicaModelSeries.getData().clear();
        unknownIris.getData().clear();
    }

    private void clearSeriesSuspectDataSetIris() {
        setosaSuspectSeries.getData().clear();
        versicolorSuspectSeries.getData().clear();
        virginicaSuspectSeries.getData().clear();
        unknownIris.getData().clear();
    }

    private Double getValueAxisIris(String str, Iris iris) {
        if (str.equals(LONGUEUR_PETALE)) {
            return iris.getPetalLength();
        } else if (str.equals(LARGEUR_PETALE)) {
            return iris.getPetalWidth();
        } else if (str.equals(LONGUEUR_SEPALE)) {
            return iris.getSepalLength();
        }
        return iris.getSepalWidth();
    }

    private void choiceIrisX() {
        choiceBoxAbscisseIris.getItems().addAll(ListIrisX);
        choiceBoxAbscisseIris.getSelectionModel().select(0);
    }

    private void choiceIrisY() {
        choiceBoxOrdonneeIris.getItems().addAll(ListIrisY);
        choiceBoxOrdonneeIris.getSelectionModel().select(1);
    }

    private String getChoiceIrisX() {
        String valueIrisX = choiceBoxAbscisseIris.getValue();
        return valueIrisX;
    }

    private String getChoiceIrisY() {
        String valueIrisY = choiceBoxOrdonneeIris.getValue();
        return valueIrisY;
    }

    public void setupDistanceAlgorithm() {
        List<DistanceAlgorithm> distanceAlgorithmList = List.of(DistanceAlgorithm.values());
        choiceBoxDistanceIris.getItems().addAll(
                distanceAlgorithmList
                        .stream()
                        .map(value -> value.toString())
                        .collect(Collectors.toList()));

        choiceBoxDistanceIris.getSelectionModel()
                .select(irisPlatformModel.getClassifier().getDistanceUsed().getAlgorithmUsed().getValue().toString());

        choiceBoxDistanceIris.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    irisPlatformModel.setDistanceAlgorithm(DistanceAlgorithm.valueOf(newValue.toString()));
                    irisPlatformModel.findBestK();
                });
    }

    public void setupDistanceNormalizedIris() {
        radioButtonNIris.selectedProperty().set(true);

        radioButtonNIris.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == true)
                irisPlatformModel.setDistanceNormalized(true);
            irisPlatformModel.findBestK();
        });

        radioButtonNNIris.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == true)
                irisPlatformModel.setDistanceNormalized(false);
            irisPlatformModel.findBestK();
        });

    }

    public void classifierIris() {
        irisPlatformModel.classify();
        irisPlatformModel.getRobustness();
    }

    public void setupBestK() {
        irisPlatformModel.getBestK().addListener((subject, data) -> {
            labelMeilleurKIris.setText(Integer.toString(irisPlatformModel.getBestK().getValue()));
            labelMaxRobustesse.setText(Double.toString(irisPlatformModel.getMaxRobustnessValue()));
        });
    }

    protected void setCellValue() {
        petalLength.setCellValueFactory(new PropertyValueFactory<Iris, Double>("petalLength"));
        petalWidth.setCellValueFactory(new PropertyValueFactory<Iris, Double>("petalWidth"));
        sepalLength.setCellValueFactory(new PropertyValueFactory<Iris, Double>("sepalLength"));
        sepalWidth.setCellValueFactory(new PropertyValueFactory<Iris, Double>("sepalWidth"));
        variety.setCellValueFactory(new PropertyValueFactory<Iris, IrisVariety>("classification"));
    }

    public void tableViewIrisTraining() {
        setCellValue();
        for (IPoint point : irisPlatformModel.getTrainingDataset().getObservablePoints()) {
            Iris iris = (Iris) point;
            irisTableView.getItems().add(iris);
        }
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

    @FXML
    void onActionAjouterUnPointIris(ActionEvent event) {
        if (!textAreaLongueurPetale.getText().isEmpty() &&
                !textAreaLargeurPetale.getText().isEmpty() &&
                !textAreaLongueurSepale.getText().isEmpty() &&
                !textAreaLargeurSepale.getText().isEmpty() &&
                isDouble(textAreaLongueurPetale.getText()) &&
                isDouble(textAreaLargeurPetale.getText()) &&
                isDouble(textAreaLongueurSepale.getText()) &&
                isDouble(textAreaLargeurSepale.getText())) {
            Double longueurPetale = Double.parseDouble(textAreaLongueurPetale.getText());
            Double largeurPetale = Double.parseDouble(textAreaLargeurPetale.getText());
            Double longueurSepale = Double.parseDouble(textAreaLongueurSepale.getText());
            Double largeurSepale = Double.parseDouble(textAreaLargeurSepale.getText());
            Iris iris = new Iris(longueurPetale, largeurPetale, longueurSepale, largeurSepale,
                    IrisVariety.UNKNOWN.getVariety());
            irisPlatformModel.addSuspectPoint(iris);
            clearTableViewSuspect();
            tableViewIrisSuspect();
        } else {
            IrisNull irisNull = new IrisNull();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("L'iris n'a pas été ajoutée !");
            alert.setContentText(irisNull.getMessage());
            alert.showAndWait();
        }
    }

    public void clearTableViewSuspect() {
        for (IPoint point : irisPlatformModel.getSuspectDataset().getPoints()) {
            Iris iris = (Iris) point;
            if (irisTableView.getItems().contains(iris)) {
                irisTableView.getItems().remove(iris);
            }
        }
    }

    public void tableViewIrisSuspect() {
        setCellValue();
        for (IPoint point : irisPlatformModel.getSuspectDataset().getPoints()) {
            Iris iris = (Iris) point;
            irisTableView.getItems().add(iris);
        }
    }

    @FXML
    void onActionResetAllIris(ActionEvent event) {
        irisTableView.getItems().clear();
        irisPlatformModel.getTrainingDataset().clear();
        irisPlatformModel.getSuspectDataset().clear();
        clearSeriesTrainingDataSetIris();
        clearSeriesSuspectDataSetIris();
        graphiqueIris.getData().clear();
        graphiqueIris.getData().addAll(setosaModelSeries, versicolorModelSeries, virginicaModelSeries,
                setosaSuspectSeries, versicolorSuspectSeries, virginicaSuspectSeries, unknownIris);
    }
}
