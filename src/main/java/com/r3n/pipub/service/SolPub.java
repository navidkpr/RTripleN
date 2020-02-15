package com.r3n.pipub.service;


import com.solacesystems.jcsmp.JCSMPException;

public interface SolPub {
    public void publish();
    public void sendMessage() throws JCSMPException;
    }
