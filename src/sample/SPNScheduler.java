package sample;

public class SPNScheduler extends Scheduler {

    public void run() {
        int schedulingTime = getSchedulingTime();
        for (int i = 0; i <= schedulingTime; i++) {
            insertQueue(i);
            // 프로세서가 idle 상태인 경우
            if (Processor.getID().equals("idle")) {
                // idle 상태 해제
                if (!queue.isEmpty()) {
                    // 초기값이 아닌 idle 상태인 경우 스케줄링 시간 추가
                    if (Processor.getArrivalTime() != 0) schedulingTime++;
                    changeProcess(i);
                }
                // idle 상태 유지
                else {
                    schedulingTime++;
                    continue;
                }
            }
            // 프로세스 종료 후 교체
            if (Processor.getBurstTime() == Processor.getRunningTime()) {
                Processor.setTurnaroundTime(i - Processor.getArrivalTime());
                changeProcess(i);
            }
            // 프로세스의 현재 누적 실행 시간 증가
            if (!Processor.getID().equals("idle")) Processor.increasRunningTime();
        }
        printResult();
    }

    public void changeProcess(int currentTime) {
        Processor.setIdleTime(currentTime);
        if(!Processor.getID().equals("idle"))
            result.add(new Process(Processor.getID(), Processor.getAwakeTime(), Processor.getIdleTime()));
        //대기 큐가 비어있지 않은 경우 프로세스 교체
        if (!queue.isEmpty()) {
            int i=0,k=0,min=Integer.MAX_VALUE;
            for(i=0;i<queue.size();i++){
                if(queue.get(i).getBurstTime()<min){
                    min=queue.get(i).getBurstTime();
                    k=i;
                }
            }
            Processor = queue.get(k);
            Processor.setAwakeTime(currentTime);
            queue.remove(k);
        }
        //비어있는 경우 프로세서를 idle 상태로 지정
        else this.setIdle(currentTime);
    }
}
