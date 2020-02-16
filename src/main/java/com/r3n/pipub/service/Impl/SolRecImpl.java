package com.r3n.pipub.service.Impl;

import com.solacesystems.jcsmp.*;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.concurrent.CountDownLatch;

@Named
public class SolRecImpl {
    final CountDownLatch latch = new CountDownLatch(1);

    @PostConstruct
    public void receive(){
        final JCSMPProperties properties = new JCSMPProperties();
        properties.setProperty(JCSMPProperties.HOST, "tcp://mr2j0vvhki1l0v.messaging.solace.cloud:20064");
        properties.setProperty(JCSMPProperties.USERNAME, "solace-cloud-client");
        properties.setProperty(JCSMPProperties.PASSWORD, "mrkfdkvj3gbk896i7rkfhf2ssq");
        properties.setProperty(JCSMPProperties.VPN_NAME, "msgvpn-2j0vvhki5mt7");
        final JCSMPSession session;
        try {
            session = JCSMPFactory.onlyInstance().createSession(properties);
            session.connect();
            final XMLMessageConsumer cons = session.getMessageConsumer(new XMLMessageListener() {

                @Override
                public void onReceive(BytesXMLMessage msg) {
                    if (msg instanceof TextMessage) {
                        System.out.printf("TextMessage received: '%s'%n",
                                ((TextMessage)msg).getText());
                    } else {
                        System.out.println("Message received.");
                    }
                    System.out.printf("Message Dump:%n%s%n",msg.dump());
                    latch.countDown();  // unblock main thread
                }

                @Override
                public void onException(JCSMPException e) {
                    System.out.printf("Consumer received exception: %s%n",e);
                    latch.countDown();  // unblock main thread
                }
            });
            final Topic topic = JCSMPFactory.onlyInstance().createTopic("Fruit");
            session.addSubscription(topic);
            cons.start();
            try {
                latch.await(); // block here until message received, and latch will flip
            } catch (InterruptedException e) {
                System.out.println("I was awoken while waiting");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
