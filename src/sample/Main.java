package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {

    private Button processAdd;
    private Button processSchedule;
    private Button processClear;
    private TextField processInputTime;
    private TextField processBurstTime;
    private TextField timeQuantum;
    private RadioButton FCFS;
    private RadioButton RR;
    private RadioButton SPN;
    private RadioButton SRTN;
    private RadioButton HRRN;
    private RadioButton MRR;
    private Parent root;
    private static GanttChart<Number, String> ganttChart;
    private static ArrayList<Process> processArrayList = new ArrayList<>();

    private int pidSequence = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("OS Process Scheduling");
        primaryStage.setScene(new Scene(root, 1500, 700));
        primaryStage.getScene().getStylesheets().add("sample/ganttchartStyle.css");
        primaryStage.show();
        initView();

        // ------------------------ [ Process Add Button ] -------------------------
        processAdd.setOnAction(actionEvent -> {
            onClickedProcessAddButton();
        });
        // --------------------------------------------------------------------------


        // -------------------------[ Process Schedule Button ] ---------------------
        processSchedule.setOnAction(actionEvent -> {
            onClickedScheduleButton();
        });
        // ---------------------------------------------------------------------------


        // ------------------------- [ Process Clear Button ] ------------------------
        processClear.setOnAction(actionEvent -> {
            onClickedProcessClearButton();
        });
        // ---------------------------------------------------------------------------
    }

    private void initView() {
        processAdd = (Button) root.lookup("#process_add");
        processSchedule = (Button) root.lookup("#scheduling_start");
        processClear = (Button) root.lookup("#process_clear");
        ganttChart = (GanttChart) root.lookup("#gantt_chart");
        processInputTime = (TextField) root.lookup("#input_time");
        processBurstTime = (TextField) root.lookup("#burst_time");
        timeQuantum = (TextField) root.lookup("#time_quantum");
        FCFS = (RadioButton) root.lookup("#FCFS");
        RR = (RadioButton) root.lookup("#RR");
        SPN = (RadioButton) root.lookup("#SPN");
        SRTN = (RadioButton) root.lookup("#SRTN");
        HRRN = (RadioButton) root.lookup("#HRRN");
        MRR = (RadioButton) root.lookup("#MRR");
        setVisiblityTimeQuantum(false);
        initRadioButton();
    }

    public void initRadioButton() {
        FCFS.setOnMouseClicked(event -> {
            setVisiblityTimeQuantum(false);
        });
        RR.setOnMouseClicked(event -> {
                    setVisiblityTimeQuantum(true);
                }
        );
        SPN.setOnMouseClicked(event -> {
                    setVisiblityTimeQuantum(false);
                }
        );
        SRTN.setOnMouseClicked(event -> {
                    setVisiblityTimeQuantum(false);
                }
        );
        HRRN.setOnMouseClicked(event -> {
                    setVisiblityTimeQuantum(false);
                }
        );
        MRR.setOnMouseClicked(event -> {
                    setVisiblityTimeQuantum(true);
                }
        );

    }

    private void activateChart(Scheduler s) {
        ArrayList<XYChart.Series<Number, String>> schedulings = new ArrayList<>();
        HashMap<String, String> colorHashMap = new HashMap<>();
        int num = 0;
        String color;
        if(processArrayList.size() <= 0)
        {
            makeAlertDialog("Process를 추가해주세요");
            return;
        }
        for (int i = 0; i < processArrayList.size(); i++) {
            processArrayList.get(i).setRunningTime(0);
            processArrayList.get(i).setTurnaroundTime(0);
            processArrayList.get(i).setIdleTime(0);
            processArrayList.get(i).setAwakeTime(0);
        }
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
        ganttChart.getYAxis().setTickLabelFill(Color.CHOCOLATE);
        ganttChart.getYAxis().setTickLabelGap(10);
        ganttChart.getData().addAll(schedulings);
        ganttChart.getYAxis().setLabel("PID");
        ganttChart.getXAxis().setLabel("Time");
        ganttChart.getYAxis().setAnimated(false);

    }

    public void setVisiblityTimeQuantum(boolean flag) {
        if (flag)
            timeQuantum.setVisible(true);
        else
            timeQuantum.setVisible(false);
    }


    public void onClickedScheduleButton() {
        Scheduler s = null;
        System.out.println("Process Scheduling Button clicked.");
        if (FCFS.isSelected()) {
            System.out.println("FCFS");
            s = new FCFSScheduler();
        } else if (RR.isSelected()) {
            if (!timeQuantum.getText().trim().equals("")) {
                System.out.println("RR");
                s = new RRScheduler(Integer.parseInt(timeQuantum.getText()));
            } else {
                makeAlertDialog("Time Quantum을 입력해주세요");
                return;
            }
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
            if (!timeQuantum.getText().trim().equals("")) {
                System.out.println("MRRN");
                s = new MRRScheduler(Integer.parseInt(timeQuantum.getText()));
            } else {
                makeAlertDialog("Time Quantum을 입력해주세요");
                return;
            }
        }

        activateChart(s);


        // ----------------------- [ Scheduling Result Table ] --------------------
        // don't forget to add factory at fxml.
        TableView resultTable = (TableView) root.lookup("#output_table");
        ((TableView) root.lookup("#output_table")).getItems().clear();
        List<ResultProcess> result = new ArrayList<>(s.result.size()); // ArrayList to List to use Collections.sort()
        HashSet<Integer> pidSet = new HashSet<>();

        for (
                int i = 0; i < s.result.size(); i++) {
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


    public void makeAlertDialog(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("알림");
        alert.setHeaderText(null);
        alert.setContentText(info);

        alert.showAndWait();
    }

    public void onClickedProcessAddButton() {
        System.out.println("Process Add Button clicked.");
        int inputTime = 0, burstTime = 0;
        if (processInputTime.getText().equals("") ||
                processBurstTime.getText().equals("")) {
            makeAlertDialog("값을 입력해주세요");
            return;
        }
        try {
            inputTime = Integer.parseInt(processInputTime.getText());
            burstTime = Integer.parseInt(processBurstTime.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }


            if (inputTime <= 0) {
                makeAlertDialog("input time은 양수이어야 합니다.");
                return;
            }
            if (burstTime <= 0) {
                makeAlertDialog("burst time은 0보다 커야됩니다.");
                return;
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

        public void onClickedProcessClearButton () {
            ((TableView) root.lookup("#process_table")).getItems().clear();
            ((TableView) root.lookup("#output_table")).getItems().clear();
            ((TextField) root.lookup("#input_time")).setText("");
            ((TextField) root.lookup("#burst_time")).setText("");
            ((TextField) root.lookup("#time_quantum")).setText("");
            processArrayList.clear();
            pidSequence = 0;
        }

        public static void main (String[]args){
            launch(args);
        }

    }
