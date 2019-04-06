public class Process {
    private String ID;
    private int arrivalTime;
    private int burstTime;
    private int runningTime;
    private int waitingTime;
    private int turnaroundTime;

    public Process(String _ID, int _arrivalTime, int _burstTime){
        setID(_ID);
        setArrivalTime(_arrivalTime);
        setBurstTime(_burstTime);
        setRunningTime(0);
        setWaitingTime(0);
        setTurnaroundTime(0);
    }

    public String getID() {
        return ID;
    }
    public void setID(String _ID) {
        this.ID = _ID;
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

    public int getWaitingTime() {
        return waitingTime;
    }
    public void setWaitingTime(int _waitingTime) {
        this.waitingTime = _waitingTime;
    }
    public void increaseWaitingTime(){
        this.waitingTime++;
    }
}

