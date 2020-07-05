package com.azure.eventmanager.service;

import org.springframework.stereotype.Component;

import com.azure.eventmanager.controller.ApplyRequest;
import com.azure.eventmanager.controller.CheckInRequest;
import com.azure.eventmanager.controller.RegisterEventRequest;
import com.azure.eventmanager.domain.Coordinates;
import com.azure.eventmanager.vo.ApplyCommand;
import com.azure.eventmanager.vo.CheckInCommand;
import com.azure.eventmanager.vo.ContactDetails;
import com.azure.eventmanager.vo.DeclineCommand;
import com.azure.eventmanager.vo.RegisterEventCommand;

import lombok.val;

@Component
public class CommandMapper {

    public RegisterEventCommand map(RegisterEventRequest request) {
        return RegisterEventCommand.builder()
                .name(request.getName())
                .address(request.getAddress())
                .organizer(request.getOrganizer())
                .platform(request.getPlatform())
                .address(request.getAddress())
                .eventStart(request.getEventStart())
                .eventEnd(request.getEventEnd())
                .totalSpots(request.getTotalSpots())
                .rate(request.getRate())
                .build();

    }

    public ApplyCommand map(ApplyRequest request) {
        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setMobilePhone(request.getMobilePhone());
        contactDetails.setEmail(request.getEmail());
        return ApplyCommand.builder()
                .eventCode(request.getEventCode())
                .memberReference(request.getMemberReference())
                .contactDetails(contactDetails)
                .build();
    }

    public CheckInCommand map(CheckInRequest request) {
        val position = new Coordinates();
        position.setLat(request.getLat());
        position.setLon(request.getLon());
        return CheckInCommand.builder()
                .position(position)
                .build();
    }
}
