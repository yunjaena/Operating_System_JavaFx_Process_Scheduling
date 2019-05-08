package sample;

public class MRRScheduler extends Scheduler {
    private int delta; // 최대 실행 시간

    public MRRScheduler(int _delta){
        setDelta(_delta);
    }

    public int getDelta() {
        return delta;
    }
    public void setDelta(int delta) {
        this.delta = delta;
    }

    public static double baseLog(double x, double base) {
        return Math.log10(x) / Math.log10(base);
    }
    public int getMercy(){
        return (int)Math.floor(baseLog(delta, 2));
    }

    public void changeProcess(int currentTime){
        Processor.setIdleTime(currentTime);
        if(Processor.getBurstTime() - Processor.getRunningTime() > 0 && Processor.getBurstTime() - Processor.getRunningTime() < getMercy() && !Processor.getID().equals("idle")){
        }
        else{
            if(!Processor.getID().equals("idle")) result.add(new Process(Processor.getID(), Processor.getAwakeTime(), Processor.getIdleTime()));
            // 프로세스 처리가 다 안됬으면 다시 큐에 삽입
            if(Processor.getBurstTime() > Processor.getRunningTime()) queue.add(Processor);
            //대기 큐가 비어있지 않은 경우 프로세스 교체
            if(!queue.isEmpty()) {
                Processor = queue.get(0);
                Processor.setAwakeTime(currentTime);
                queue.remove(0);
            }
            //비어있는 경우 프로세서를 idle 상태로 지정
            else this.setIdle(currentTime);
        }

    }

    public void run(){
        int runOutTimer = 0;
        int schedulingTime = getSchedulingTime();

        for(int i = 0; i < schedulingTime + 1; i++){
            insertQueue(i);
            if(Processor.getID().equals("idle")){
                // idle 상태 해제
                if(!queue.isEmpty()){
                    // 초기값이 아닌 idle 상태인 경우 스케줄링 시간 추가
                    if(Processor.getArrivalTime() != 0) schedulingTime++;
                    changeProcess(i);
                }
                // idle 상태 유지
                else{
                    schedulingTime++;
                    continue;
                }
            }
            // 프로세스 종료 후 또는 런아웃 타이머에 의해 교체
            if(Processor.getBurstTime() == Processor.getRunningTime() || runOutTimer == this.getDelta()){
                Processor.setTurnaroundTime(i - Processor.getArrivalTime());
                changeProcess(i);
                runOutTimer = 0;
            }
            // 프로세스의 현재 누적 실행 시간 및 런아웃타이머 증가
            if(!Processor.getID().equals("idle")){
                Processor.increasRunningTime();
                runOutTimer++;
            }
        }
        printResult();
    }
}