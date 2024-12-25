package com.modsen.booktrackerservice.service;

public interface RabbitListenerService {

    public void receiveCreateMessage(String message);

    public void receiveDeleteMessage(String message);
}
