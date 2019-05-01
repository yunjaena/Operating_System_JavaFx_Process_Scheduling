package sample;

public class Process {
    private String PID;
    private int arrivalTime; // 프로세스가 도착한 시간
    private int burstTime; // 프로세스의 총 실행 시간
    private int runningTime; // 현재까지 프로세스가 실행된 누적 시간
    private int turnaroundTime; // 프로세스의 완전 종료 시간
    private int idleTime; // 프로세스가 외부 요인에 의해 idle 상태가 된 시점
    private int awakeTime; // 프로세스가 다시 깨어난 시점

    public Process(String _PID, int _arrivalTime, int _burstTime){
        setID(_PID);
        setArrivalTime(_arrivalTime);
        setBurstTime(_burstTime);
        setRunningTime(0);
        setIdleTime(0);
        setTurnaroundTime(0);
    }

    public String getPID() {
        return PID;
    }
    public void setID(String _PID) {
        this.PID = _PID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(int _arrivalTime) {
        this.arrivalTime = _arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }
    public void setBurstTime(int _burstTime) {
        this.burstTime = _burstTime;
    }

    public int getRunningTime() {
        return runningTime;
    }
    public void setRunningTime(int _runningTime) {
        this.runningTime = _runningTime;
    }
    public void increasRunningTime(){
        this.runningTime++;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }
    public void setTurnaroundTime(int _turnaroundTime) {
        this.turnaroundTime = _turnaroundTime;
    }

    public int getIdleTime() {
        return idleTime;
    }
    public void setIdleTime(int idleTime) {
        this.idleTime = idleTime;
    }

    public int getAwakeTime() {
        return awakeTime;
    }
    public void setAwakeTime(int awakeTime) {
        this.awakeTime = awakeTime;
    }

    public int getWaitingTime(){
        return this.getTurnaroundTime() - this.getBurstTime();
    }
    public float getNTT(){
        return (float)getTurnaroundTime()/getBurstTime();
    }
}