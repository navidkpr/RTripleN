package com.r3n.pipub;

import com.r3n.pipub.service.SolPub;
import com.solacesystems.jcsmp.JCSMPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
public class PipubApplication {

    @Autowired
    SolPub publisher;
    public static void main(String[] args) {
        SpringApplication.run(PipubApplication.class, args);
        //publisher.publish();
    }

    @Scheduled(fixedDelay = 5000)
    public void scheduledRunner() throws JCSMPException {
        publisher.sendMessage();
    }

}
