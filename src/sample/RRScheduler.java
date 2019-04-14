public class RRScheduler extends Scheduler {
    private int delta;

    public RRScheduler(int _delta){
        setDelta(_delta);
    }

    public int getDelta() {
        return delta;
    }
    public void setDelta(int delta) {
        this.delta = delta;
    }

    public void changeProcess(int currentTime){
        runningProcess.setIdleTime(currentTime);
        result.add(new Process(runningProcess.getID(), runningProcess.getArrivalTime(), runningProcess.getBurstTime(), runningProcess.getIdleTime(), runningProcess.getAwakeTime()));
        if(!queue.isEmpty()) {
            if(runningProcess.getBurstTime() > runningProcess.getRunningTime()) queue.add(runningProcess); // 프로세스 처리가 다 안됬으면 다시 큐에 삽입
            runningProcess = queue.get(0);
            runningProcess.setAwakeTime(currentTime);
            queue.remove(0);
        }
        else this.setIdle();
    }

    public void run(){
        int runoutTimer = 0;
        int schedulingTime = getSchedulingTime();

        for(int i = 0; i < schedulingTime + 1; i++){
            insertQueue(i);
            if(runningProcess.getID().equals("idle") && !queue.isEmpty()){
                runningProcess = queue.get(0);
                queue.remove(0);
            }
            if(runningProcess.getBurstTime() == runningProcess.getRunningTime() || runoutTimer == this.getDelta()){
                runningProcess.setTurnaroundTime(i - runningProcess.getArrivalTime());
                changeProcess(i);
                runoutTimer = 0;
            }
            if(!runningProcess.getID().equals("idle")){
                runningProcess.increasRunningTime();
                runoutTimer++;
            }
        }
        printResult();
    }
}
