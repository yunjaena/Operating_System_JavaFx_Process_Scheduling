import java.util.ArrayList;
import java.util.Iterator;

public class Scheduler {
    ArrayList<Process> pArr; //프로세스 원본 배열
    ArrayList<Process> queue;
    ArrayList<Process> result; //결과 저장용

    public Scheduler(){
        pArr = new ArrayList<Process>();
        queue = new ArrayList<Process>();
        result = new ArrayList<Process>();
    }

    public void insertProcess(Process pi){
        pArr.add(pi);
    }

    public void insertQueue(int currentTime){
        for(int i = 0; i < pArr.size(); i++){
            if(pArr.get(i).getArrivalTime() == currentTime) queue.add(pArr.get(i));
        }
    }

    public void increaseWaitingTime(){
        for(int i = 0; i < queue.size(); i++){
            queue.get(i).increaseWaitingTime();
        }
    }

    public void printResult(){
        for(int i = 0; i < result.size(); i++){
            System.out.println(result.get(i).getID() + ": " + (result.get(i).getWaitingTime() + result.get(i).getArrivalTime()) + " - " + result.get(i).getTurnaroundTime());
        }
    }

    public void FCFS(){
        Process idle = new Process("idle", 0, 99); // 대기 상태
        Process run = idle;
        int totalBT = 0;
        for(int i = 0;  i< pArr.size(); i++){
            totalBT += pArr.get(i).getBurstTime();
        }
        for(int i = 0; i < totalBT + 1; i++){
            insertQueue(i);
            if(run.getID().equals("idle") && !queue.isEmpty()){
                run = queue.get(0);
                queue.remove(0);
            }
            if(run.getBurstTime() == run.getRunningTime()){
                run.setTurnaroundTime(i);
                result.add(run);
                if(!queue.isEmpty()){
                    run = queue.get(0);
                    queue.remove(0);
                }
                else run = idle;
            }
            if(!run.getID().equals("idle")){
                run.increasRunningTime();
                increaseWaitingTime();
            }
        }
        printResult();
    }
    public static void main(String args[]){
        Scheduler a = new Scheduler();
        a.insertProcess(new Process("1", 0, 3));
        a.insertProcess(new Process("2", 1, 7));
        a.insertProcess(new Process("3", 3, 2));
        a.insertProcess(new Process("4", 5, 5));
        a.insertProcess(new Process("5", 6, 3));

        a.FCFS();
    }
}


