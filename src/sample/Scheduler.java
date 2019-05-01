package sample;

import java.util.ArrayList;
import sample.Process;

public abstract class Scheduler {
    ArrayList<Process> pArr; //프로세스 원본 배열(결과 표 출력용 데이터가 저장됨)
    ArrayList<Process> queue; // 프로세스 대기열
    ArrayList<Process> result; //전체 프로세싱 과정 저장(그래프 출력용 데이터가 저장됨)
    Process Processor; // 프로세서

    public Scheduler(){
        pArr = new ArrayList<Process>();
        queue = new ArrayList<Process>();
        result = new ArrayList<Process>();
        setIdle(0); // 대기 상태
    }
    // 프로세서를 idle 상태로 지정
    public void setIdle(int i){
        Processor = new Process("idle", i, 0);
        Processor.setAwakeTime(i);
    }
    // 총 버스트 타임 = 총 스케줄링 타임
    public int getSchedulingTime() {
        int totalBT = 0;
        for (int i = 0; i < pArr.size(); i++) {
            totalBT += pArr.get(i).getBurstTime();
        }
        return totalBT;
    }
    // 원본 배열에 프로세스 삽입
    public void insertProcess(Process pi){
        pArr.add(pi);
    }
    //현재 시간과 도착 시간이 일치하는 프로세스를 큐에 삽입
    public void insertQueue(int currentTime){
        for(int i = 0; i < pArr.size(); i++){
            if(pArr.get(i).getArrivalTime() == currentTime) queue.add(pArr.get(i));
        }
    }

    public void printResult(){
        for(int i = 0; i < result.size(); i++){// 전체 프로세스 스케줄링과정 출력(출력 값 가져다 그래프로 시각화)
            System.out.print("[" + result.get(i).getID() + ": " + result.get(i).getArrivalTime() + " - " + result.get(i).getBurstTime() + "] ");
        }
        System.out.println();
        for(int i = 0; i < pArr.size(); i++){// 프로세스별 AT, BT, WT, TT, NTT 출력(출력 값 가져다 표로 시각화)
            System.out.println("[" + pArr.get(i).getID() + "] AT : " + pArr.get(i).getArrivalTime() + "  BT : " + pArr.get(i).getBurstTime() + "  WT : " + pArr.get(i).getWaitingTime() + "  TT : " + pArr.get(i).getTurnaroundTime() + "  NTT : " + pArr.get(i).getNTT());
        }
    }

    public void changeProcess(int currentTime){
        Processor.setIdleTime(currentTime);
        if(Processor.getArrivalTime() != Processor.getIdleTime()) result.add(new Process(Processor.getID(), Processor.getAwakeTime(), Processor.getIdleTime()));
        //대기 큐가 비어있지 않은 경우 프로세스 교체
        if(!queue.isEmpty()) {
            Processor = queue.get(0);
            Processor.setAwakeTime(currentTime);
            queue.remove(0);
        }
        //비어있는 경우 프로세서를 idle 상태로 지정
        else this.setIdle(currentTime);
    }
    public abstract void run();

    /*public static void main(String args[]){
        Scheduler a = new FCFSScheduler();
        a.insertProcess(new Process("1", 1, 3));
        a.insertProcess(new Process("2", 2, 2));
        a.run();
    }*/
}


