package com.example.tanga.driverprotection.io;

public interface ObdProgressListener {

    void stateUpdate(final ObdCommandJob job);

}