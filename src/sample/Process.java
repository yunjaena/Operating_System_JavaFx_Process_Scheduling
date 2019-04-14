public class Process {
    private String ID;
    private int arrivalTime;
    private int burstTime;
    private int runningTime;
    private int turnaroundTime;
    private int idleTime;
    private int awakeTime;

    public Process(String _ID, int _arrivalTime, int _burstTime){
        setID(_ID);
        setArrivalTime(_arrivalTime);
        setBurstTime(_burstTime);
        setRunningTime(0);
        setIdleTime(0);
        setTurnaroundTime(0);
    } //처음 삽입용

    public Process(String _ID, int _arrivalTime, int _burstTime, int _idleTime, int _awakeTime){
        setID(_ID);
        setArrivalTime(_arrivalTime);
        setBurstTime(_burstTime);
        setIdleTime(_idleTime);
        setAwakeTime(_awakeTime);
    } //중간 결과 저장용

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

