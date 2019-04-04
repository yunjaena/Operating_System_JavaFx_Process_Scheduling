package sample;

public class Process {
    private int PID = 0;
    private int inputTime = 0;
    private int burstTime = 0;

    public Process() {
    }

    public Process(int _PID, int _inputTime, int _burstTime) {
        setPID(_PID);
        setInputTime(_inputTime);
        setBurstTime(_burstTime);
    }

    public int getPID() {
        return PID;
    }
    public void setPID(int _PID) {
        this.PID = _PID;
    }

    public int getInputTime() {
        return inputTime;
    }
    public void setInputTime(int _inputTime) {
        this.inputTime = _inputTime;
    }

    public int getBurstTime() {
        return burstTime;
    }
    public void setBurstTime(int _burstTime) {
        this.burstTime = _burstTime;
    }
}
