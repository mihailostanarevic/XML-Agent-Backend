package com.rentacar.agentbackend.scheduler;

import com.rentacar.agentbackend.service.ICarGSPService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronJob {

    private final ICarGSPService _carGSPService;

    public CronJob(ICarGSPService carGSPService) {
        _carGSPService = carGSPService;
    }

    @Scheduled(cron="0 * * ? * *") //every minute
//    @Scheduled(cron="0 * * * * *") //every hour
    public void cronJobTruck() throws Exception {
        _carGSPService.tracking();
    }
}
