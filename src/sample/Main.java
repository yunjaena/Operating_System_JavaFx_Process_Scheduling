package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    private Button processAdd;
    private Button processSchedule;
    private TextField processInputTime;
    private TextField processBurstTime;

    private ArrayList<Process> processArrayList = new ArrayList<>();

    private int pidSequence=0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("OS Process Scheduling");
        primaryStage.setScene(new Scene(root, 700, 700));
        primaryStage.show();

        processInputTime = (TextField)root.lookup("#input_time");
        processBurstTime = (TextField)root.lookup("#burst_time");

        processAdd = (Button)root.lookup("#process_add");
        processAdd.setOnAction(actionEvent ->  {
            System.out.println("Process Add Button clicked.");
            int inputTime=0, burstTime=0;
            try{
                inputTime = Integer.parseInt(processInputTime.getText());
                burstTime = Integer.parseInt(processBurstTime.getText());
            } catch(Exception e){}
            System.out.printf("inputTime is %d, burstTime is %d\n", inputTime, burstTime);

            TableView processTable = (TableView)root.lookup("#process_table");
            Process input = new Process(Integer.toString(pidSequence), inputTime, burstTime);
            processTable.getItems().add(input);
            processArrayList.add(input);
            pidSequence++;
        });

        processSchedule = (Button)root.lookup("#scheduling_start");
        processSchedule.setOnAction(actionEvent ->  {
            System.out.println("Process Scheduling Button clicked.");
            RadioButton FCFS = (RadioButton)root.lookup("#FCFS");
            RadioButton RR = (RadioButton)root.lookup("#RR");
            RadioButton SPN = (RadioButton)root.lookup("#SPN");
            RadioButton SRTN = (RadioButton)root.lookup("#SRTN");
            RadioButton HRRN = (RadioButton)root.lookup("#HRRN");
            RadioButton FTW = (RadioButton)root.lookup("#FTW");
            if(FCFS.isSelected()){
                System.out.println("FCFS");
            }
            else if(RR.isSelected()){
                System.out.println("RR");
            }
            else if(SPN.isSelected()){
                System.out.println("SPN");
            }
            else if(SRTN.isSelected()){
                System.out.println("SRTN");
            }
            else if(HRRN.isSelected()){
                System.out.println("HRRN");
            }
            else if(FTW.isSelected()){
                System.out.println("FTW");
            }
        });

        
    }


    public static void main(String[] args) {
        launch(args);
    }
}
