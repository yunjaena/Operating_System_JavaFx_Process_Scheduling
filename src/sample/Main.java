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

    private Button processAdd;
    private Button processSchedule;
    private TextField processInputTime;
    private TextField processBurstTime;
    private TextField timeQuantom;
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
    private RadioButton MRR;
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
        timeQuantom = (TextField) root.lookup("#time_quantom");
        FCFS = (RadioButton) root.lookup("#FCFS");
        RR = (RadioButton) root.lookup("#RR");
        SPN = (RadioButton) root.lookup("#SPN");
        SRTN = (RadioButton) root.lookup("#SRTN");
        HRRN = (RadioButton) root.lookup("#HRRN");
        MRR = (RadioButton) root.lookup("#MRR");
    }

    private void activateChart(Scheduler s) {
        ArrayList<XYChart.Series<Number, String>> schedulings = new ArrayList<>();
        HashMap<String, String> colorHashMap = new HashMap<>();
        int num = 0;
        String color;
        s.pArr.addAll(processArrayList);
        s.run();

        for (Process process : s.result) {
            XYChart.Series<Number, String> scheduling = new XYChart.Series<>();
            scheduling.setName(process.getPID());
            if (colorHashMap.isEmpty()) {
                num++;
                color = "status-0";
                colorHashMap.put(process.getPID(), color);
            } else if (colorHashMap.containsKey(process.getPID()))
                color = colorHashMap.get(process.getPID());
            else {
                color = "status-" + num % 11;
                colorHashMap.put(process.getPID(), color);
                num++;

            }
            scheduling.getData().add(new XYChart.Data<>(process.getArrivalTime(), process.getPID(), new GanttChart.ExtraData(process.getBurstTime() - process.getArrivalTime(), color)));
            schedulings.add(scheduling);
        }

        //ganttChart.getStylesheets().add(getClass().getResource("ganttchartStyle.css").toExternalForm());
        ganttChart.getData().clear();
        ganttChart.layout();
        ganttChart.getData().addAll(schedulings);


    }


    private ArrayList<XYChart.Series<Number, String>> runScheduling(Scheduler s) {
        ArrayList<XYChart.Series<Number, String>> schedulings = new ArrayList<>();
        s.pArr = processArrayList;
        s.run();
        for (int i = 0; i < s.result.size(); i++) {
            XYChart.Series<Number, String> scheduling = new XYChart.Series<>();
            // < ! REMINDER ! >
            // BurstTime is ENDTIME of segment. ArrivalTime is STARTTIME of segment.
            int takenTime = s.result.get(i).getBurstTime() - s.result.get(i).getArrivalTime();
            // -------------------------------------------------------------------------------
            scheduling.getData().add(new XYChart.Data<>(takenTime, ""));
            schedulings.add(scheduling);
        }


        return schedulings;
    }


    public void onClickedScheduleButton() {
        ArrayList<XYChart.Series<Number, String>> schedulings = null;
        Scheduler s = null;
        System.out.println("Process Scheduling Button clicked.");
        if (FCFS.isSelected()) {
            System.out.println("FCFS");
            s = new FCFSScheduler();
        } else if (SPN.isSelected()) {
            System.out.println("SPN");
            s = new SPNScheduler();
        } else if (SRTN.isSelected()) {
            System.out.println("SRTN");
            s = new SRTNScheduler();
        } else if (HRRN.isSelected()) {
            System.out.println("HRRN");
            s = new HRRNScheduler();
        } else if (MRR.isSelected()) {
            System.out.println("MRRN");
            s = new MRRScheduler(Integer.parseInt(timeQuantom.getText()));
        }


        activateChart(s);


        // ----------------------- [ Scheduling Result Table ] --------------------
        // don't forget to add factory at fxml.
        // TODO: Process Preemptive, Non-Preemptive scheduling differently. Need to count time.
        TableView resultTable = (TableView) root.lookup("#output_table");
        List<ResultProcess> result = new ArrayList<>(s.result.size()); // ArrayList to List to use Collections.sort()
        HashSet<Integer> pidSet = new HashSet<>();

        for (int i = 0; i < s.result.size(); i++) {
            Process process = s.result.get(i); // process of scheduling output. maybe divided into multiple parts.
            Process origin = null;             // process of scheduling input.
            for (int j = 0; j < s.pArr.size(); j++) {
                if (s.pArr.get(j).getPID().equals(process.getPID())) {
                    origin = s.pArr.get(j);
                    break;
                } // look for matching PID and process of current output process.
            }

            if (origin != null) {
                if (pidSet.add(Integer.parseInt(origin.getPID()))) {
                    result.add(new ResultProcess(process.getPID(), origin.getTurnaroundTime() - origin.getBurstTime(),
                            origin.getTurnaroundTime(), (float) origin.getTurnaroundTime() / origin.getBurstTime()));
                } // if process is duplicated in output list, ignore.
            }
        }

        Collections.sort(result); // check compareTo method in ResultProcess.java
        resultTable.getItems().addAll(result);
        // ------------------------------------------------------------------------

    }


    public void onClickedProccessAddButton() {
        System.out.println("Process Add Button clicked.");
        int inputTime = 0, burstTime = 0;
        try {
            inputTime = Integer.parseInt(processInputTime.getText());
            burstTime = Integer.parseInt(processBurstTime.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // try-catch for unexpected(String, etc...) input value.
        System.out.printf("inputTime is %d, burstTime is %d\n", inputTime, burstTime);
        // print at console for debugging.

        // ------------------------ [ Process Table Management ] ---------------------------
        TableView processTable = (TableView) root.lookup("#process_table");
        Process input = new Process(Integer.toString(pidSequence), inputTime, burstTime);
        processTable.getItems().add(input);
        processArrayList.add(input);
        pidSequence++;

        // get TableView and insert Process. Add to arraylist too.
        processInputTime.setText("");
        processBurstTime.setText("");
    }


    public static void main(String[] args) {
        launch(args);
    }

}
