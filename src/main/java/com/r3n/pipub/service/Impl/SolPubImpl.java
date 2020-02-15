package com.r3n.pipub.service.Impl;

import com.r3n.pipub.service.SolPub;
import com.solacesystems.jcsmp.*;

import javax.annotation.PostConstruct;
import javax.inject.Named;

@Named
public class SolPubImpl implements SolPub {
    XMLMessageProducer prod;

    @PostConstruct
    public void publish(){
        final JCSMPProperties properties = new JCSMPProperties();
        properties.setProperty(JCSMPProperties.HOST, "tcp://mr2j0vvhki1l0v.messaging.solace.cloud:20064");
        properties.setProperty(JCSMPProperties.USERNAME, "solace-cloud-client");
        properties.setProperty(JCSMPProperties.PASSWORD, "mrkfdkvj3gbk896i7rkfhf2ssq");
        properties.setProperty(JCSMPProperties.VPN_NAME, "msgvpn-2j0vvhki5mt7");
        final JCSMPSession session;
        try {
            session = JCSMPFactory.onlyInstance().createSession(properties);
            session.connect();
            prod = session.getMessageProducer(new JCSMPStreamingPublishEventHandler() {

                @Override
                public void responseReceived(String messageID) {
                    System.out.println("Producer received response for msg: " + messageID);
                }

                @Override
                public void handleError(String messageID, JCSMPException e, long timestamp) {
                    System.out.printf("Producer received error for msg: %s@%s - %s%n",
                            messageID,timestamp,e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void sendMessage() throws JCSMPException {
        System.out.println("sending message");
        final Topic topic = JCSMPFactory.onlyInstance().createTopic("try-me");
        TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
        final String text = "Hello world!";
        msg.setText(text);
        prod.send(msg,topic);
    }
}
