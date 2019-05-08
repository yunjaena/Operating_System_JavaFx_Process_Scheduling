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

import java.util.*;

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
    private RadioButton MRRN;
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
        timeQuantom = (TextField) root.lookup("#time_quantom");
        FCFS = (RadioButton) root.lookup("#FCFS");
        RR = (RadioButton) root.lookup("#RR");
        SPN = (RadioButton) root.lookup("#SPN");
        SRTN = (RadioButton) root.lookup("#SRTN");
        HRRN = (RadioButton) root.lookup("#HRRN");
        MRRN = (RadioButton) root.lookup("#FTW");
    }

    private ArrayList<XYChart.Series<Number, String>> runScheduling(Scheduler s) {
        ArrayList<XYChart.Series<Number, String>> schedulings = new ArrayList<>();
        s.pArr = processArrayList;
        s.run();
        for(int i=0;i<s.result.size();i++){
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
        System.out.println("Process Scheduling Button clicked.");
        ArrayList<XYChart.Series<Number, String>> schedulings = null;
        Scheduler s = null;

        if (FCFS.isSelected()) {
            System.out.println("FCFS");
            s = new FCFSScheduler();
        } else if (RR.isSelected()) {
            System.out.println("RR");
            s = new RRScheduler(Integer.parseInt(timeQuantom.getText()));
        } else if (SPN.isSelected()) {
            System.out.println("SPN");
            s = new SPNScheduler();
        } else if (SRTN.isSelected()) {
            System.out.println("SRTN");
            s = new SRTNScheduler();
        } else if (HRRN.isSelected()) {
            System.out.println("HRRN");
        } else if (MRRN.isSelected()) {
            System.out.println("MRRN");
        }

        schedulings = runScheduling(s);
        ganttChart.getData().addAll(schedulings);

        for (Node n : ganttChart.lookupAll(".default-color0.chart-bar")) {
            n.setStyle("-fx-bar-fill: yellow;");
        }

        // ----------------------- [ Scheduling Result Table ] --------------------
        // don't forget to add factory at fxml.
        // TODO: Process Preemptive, Non-Preemptive scheduling differently. Need to count time.
        TableView resultTable = (TableView) root.lookup("#output_table");
        List<ResultProcess> result = new ArrayList<>(s.result.size()); // ArrayList to List to use Collections.sort()
        HashSet<Integer> pidSet = new HashSet<>();

        for(int i=0;i<s.result.size();i++){
            Process process = s.result.get(i); // process of scheduling output. maybe divided into multiple parts.
            Process origin = null;             // process of scheduling input.
            for(int j=0;j<s.pArr.size();j++){
                if(s.pArr.get(j).getPID().equals(process.getPID())){
                    origin = s.pArr.get(j);
                    break;
                } // look for matching PID and process of current output process.
            }

            if(origin != null){
                if( pidSet.add(Integer.parseInt(origin.getPID())) ){
                    result.add(new ResultProcess(process.getPID(),origin.getTurnaroundTime() - origin.getBurstTime(),
                            origin.getTurnaroundTime(),(float)origin.getTurnaroundTime()/origin.getBurstTime()));
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
        } catch (Exception e) { e.printStackTrace();
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
        // ----------------------------------------------------------------------------------

        processInputTime.setText("");
        processBurstTime.setText("");
        // blank input field.
    }

    public static void main(String[] args) {
        launch(args);
    }
}
