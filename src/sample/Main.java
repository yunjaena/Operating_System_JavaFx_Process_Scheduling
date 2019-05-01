package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import sample.FCFSScheduler;

public class Main extends Application {

    private FCFSScheduler fcfs;
    private Button processAdd;
    private Button processSchedule;
    private TextField processInputTime;
    private TextField processBurstTime;
    private RadioButton FCFS;
    private RadioButton RR;
    private RadioButton SPN;
    private RadioButton SRTN;
    private RadioButton HRRN;
    private RadioButton FTW;
    private StackedBarChart<Number, String> ganttChart;
    private CategoryAxis processList;
    private NumberAxis processTime;
    private Parent root;

    private ArrayList<Process> processArrayList = new ArrayList<>();

    private int pidSequence = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("OS Process Scheduling");
        primaryStage.setScene(new Scene(root, 700, 700));
        primaryStage.getScene().getStylesheets().add("sample/ganttchartStyle.css");
        primaryStage.show();
        initView();


        // ----------------------- [ Stacked Bar Chart components ] ----------------
        processList = new CategoryAxis();
        processTime = new NumberAxis();

        processList.setLabel("category");
        processTime.setLabel("Process scheduling.");

        var processKind = FXCollections.<String>observableArrayList(Arrays.asList("PROCESS"));
        processList.setCategories(processKind);
        // -------------------------------------------------------------------------

        // ------------------------ [ Process Add Button ] -------------------------
        processAdd = (Button) root.lookup("#process_add");
        processAdd.setOnAction(actionEvent -> {
            onClickedProccessAddButton();
        });
        // --------------------------------------------------------------------------


        // -------------------------[ Process Schedule Button ] ---------------------
        processSchedule = (Button) root.lookup("#scheduling_start");
        processSchedule.setOnAction(actionEvent -> {
            onClickedScheduleButton();
        });
        // ---------------------------------------------------------------------------

    }

    private void initView() {
        ganttChart = (StackedBarChart) root.lookup("#gantt_chart");
        processInputTime = (TextField) root.lookup("#input_time");
        processBurstTime = (TextField) root.lookup("#burst_time");
        FCFS = (RadioButton) root.lookup("#FCFS");
        RR = (RadioButton) root.lookup("#RR");
        SPN = (RadioButton) root.lookup("#SPN");
        SRTN = (RadioButton) root.lookup("#SRTN");
        HRRN = (RadioButton) root.lookup("#HRRN");
        FTW = (RadioButton) root.lookup("#FTW");
    }

    private static void fcfs(Scheduler s) {

    }

    public void onClickedScheduleButton() {
        System.out.println("Process Scheduling Button clicked.");

        ArrayList<XYChart.Series<Number, String>> schedulings = new ArrayList<>();

        if (FCFS.isSelected()) {
            System.out.println("FCFS");
              /*  fcfs(new FCFSScheduler());
                Scheduler s = new FCFSScheduler();
                Collections.copy(s.pArr, processArrayList);
                s.run();
                for(int i=0;i<s.result.size();i++){

                }*/
        } else if (RR.isSelected()) {
            System.out.println("RR");
        } else if (SPN.isSelected()) {
            System.out.println("SPN");
        } else if (SRTN.isSelected()) {
            System.out.println("SRTN");
        } else if (HRRN.isSelected()) {
            System.out.println("HRRN");
        } else if (FTW.isSelected()) {
            System.out.println("FTW");
        }


        for (int i = 0; i < 3; i++) {
            XYChart.Series<Number, String> scheduling = new XYChart.Series<>();
            // XYChart.Data<Number, String> data = new XYChart.Data<>(10, "PROCESS 0");
            scheduling.getData().add(new XYChart.Data<>(10, ""));
            schedulings.add(scheduling);
        }

        //ganttChart.getData().addAll(scheduling1, scheduling2, scheduling3);
        ganttChart.getData().addAll(schedulings);

        for (Node n : ganttChart.lookupAll(".default-color0.chart-bar")) {
            n.setStyle("-fx-bar-fill: yellow;");
        }
    }

    public void onClickedProccessAddButton() {
        System.out.println("Process Add Button clicked.");
        int inputTime = 0, burstTime = 0;
        try {
            inputTime = Integer.parseInt(processInputTime.getText());
            burstTime = Integer.parseInt(processBurstTime.getText());
        } catch (Exception e) {
        }
        // try-catch for unexpected(String, etc...) input value.
        System.out.printf("inputTime is %d, burstTime is %d\n", inputTime, burstTime);
        // print at console for debugging.

        TableView processTable = (TableView) root.lookup("#process_table");
        Process input = new Process(Integer.toString(pidSequence), inputTime, burstTime);
        processTable.getItems().add(input);
        processArrayList.add(input);
        pidSequence++;
        // get TableView and insert Process. Add to arraylist too.
    }

    public void fcfsGanttChart()
    {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
