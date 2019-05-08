package sample;

public class ResultProcess implements Comparable<ResultProcess> {
    private String PID;
    private int waitingTime;
    private int turnaroundTime;
    private float NTT;

    public ResultProcess(String _PID, int _waitingTime, int _turnaroundTime, float _NTT){
        PID = _PID;
        waitingTime = _waitingTime;
        turnaroundTime  = _turnaroundTime;
        NTT = _NTT;
    }

    public String getPID() {
        return PID;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public float getNTT() {
        return NTT;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public void setNTT(float NTT) {
        this.NTT = NTT;
    }

    @Override
    public int compareTo(ResultProcess s) {
        int thisPID = Integer.parseInt(this.PID);
        int thatPID = Integer.parseInt(s.PID);
        if (thisPID < thatPID) {
            return -1;
        } else if (thisPID > thatPID) {
            return 1;
        }
        return 0;
    }
    // 출처: https://includestdio.tistory.com/35 [includestdio]
}
