package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

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
    final NumberAxis xAxis = new NumberAxis();
    final CategoryAxis yAxis = new CategoryAxis();
    @FXML
    private static GanttChart<Number, String> ganttChart;
    private CategoryAxis processList;
    private NumberAxis processTime;
    private Parent root;
    private static ArrayList<Process> processArrayList = new ArrayList<>();

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
        ganttChart = (GanttChart) root.lookup("#gantt_chart");
        processInputTime = (TextField) root.lookup("#input_time");
        processBurstTime = (TextField) root.lookup("#burst_time");
        FCFS = (RadioButton) root.lookup("#FCFS");
        RR = (RadioButton) root.lookup("#RR");
        SPN = (RadioButton) root.lookup("#SPN");
        SRTN = (RadioButton) root.lookup("#SRTN");
        HRRN = (RadioButton) root.lookup("#HRRN");
        FTW = (RadioButton) root.lookup("#FTW");
    }

    private void activateChart(Scheduler s) {
        ArrayList<XYChart.Series<Number, String>> schedulings = new ArrayList<>();
        HashMap<String, String> colorHashMap = new HashMap<>();
        int num = 0;
        String color;
        s.pArr = processArrayList;
        s.run();

        for (Process process : s.result) {
            XYChart.Series<Number, String> scheduling = new XYChart.Series<>();
            scheduling.setName(process.getPID());
            if(colorHashMap.isEmpty()) {
                num++;
                color = "status-0";
                colorHashMap.put(process.getPID(),color);
            }
            else if(colorHashMap.containsKey(process.getPID()))
                color = colorHashMap.get(process.getPID());
            else
            {
                color = "status-"+num%11;
                colorHashMap.put(process.getPID(),color);
                num++;

            }
            scheduling.getData().add(new XYChart.Data<>(process.getArrivalTime(), process.getPID(), new GanttChart.ExtraData(process.getBurstTime() - process.getArrivalTime(), color)));
            schedulings.add(scheduling);
        }

        //ganttChart.getStylesheets().add(getClass().getResource("ganttchartStyle.css").toExternalForm());
        ganttChart.getData().addAll(schedulings);


    }

    public void setColor(ArrayList<XYChart.Series<Number, String>> schedulings) {

    }

    public void onClickedScheduleButton() {
        System.out.println("Process Scheduling Button clicked.");


        if (FCFS.isSelected()) {
            System.out.println("FCFS");
            activateChart(new FCFSScheduler());
        } else if (RR.isSelected()) {
            System.out.println("RR");
//            activateChart();
        } else if (SPN.isSelected()) {
            System.out.println("SPN");
        } else if (SRTN.isSelected()) {
            System.out.println("SRTN");
        } else if (HRRN.isSelected()) {
            System.out.println("HRRN");
        } else if (FTW.isSelected()) {
            System.out.println("FTW");
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

    public void fcfsGanttChart() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
