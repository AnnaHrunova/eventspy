package com.azure.eventmanager.controller;

public interface EventSpy {

    interface View {
        String EVENTS = "events";
        String ADD_EVENT = "add_event";
        String EVENT_DETAILS = "event_details";
        String CHECK_IN = "check_in";
    }

    interface Model {
        String EVENTS = "events";
        String EVENT = "event";
        String EVENT_DETAILS = "eventDetails";
        String APPLICATION_REFERENCE = "applicationReference";
    }
}
