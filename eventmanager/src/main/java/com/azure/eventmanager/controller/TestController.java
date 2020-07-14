package com.azure.eventmanager.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.eventmanager.domain.Coordinates;
import com.azure.eventmanager.service.CommunicationService;
import com.azure.eventmanager.service.EventManagerService;
import com.azure.eventmanager.service.MemberService;
import com.azure.eventmanager.vo.ApplyCommand;
import com.azure.eventmanager.vo.CheckInCommand;
import com.azure.eventmanager.vo.ContactDetails;
import com.azure.eventmanager.vo.RegisterEventCommand;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@RestController(value = "test")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class TestController {

    private final MemberService memberService;
    private final EventManagerService eventManagerService;
    private final CommunicationService communicationService;

    private static final String organizer = "organizer@organizer.com";
    private static final String member = "member-1";

    @GetMapping(value = "/prepare-test")
    public ResponseEntity<String> testPrepare() {
        val registerEventCommand = RegisterEventCommand.builder()
                .address("Riga Nicgales 38")
                .name("Event - 1")
                .eventStart(LocalDateTime.now().plusHours(2))
                .eventEnd(LocalDateTime.now().plusHours(5))
                .organizer(organizer)
                .totalSpots(20)
                .platform("Platform-1")
                .rate(new BigDecimal("0.7"))
                .build();
        val code = eventManagerService.registerEvent(registerEventCommand);
        return ResponseEntity.ok(code);
    }

    @GetMapping(value = "/apply-test/{eventCode}")
    public ResponseEntity<String> testApply(@PathVariable String eventCode) {

        val testContactDetails = new ContactDetails("email1@email.lv", "123456");
        val applyForEventCommand = ApplyCommand.builder()
                .eventCode(eventCode)
                .memberReference(member)
                .contactDetails(testContactDetails)
                .build();

        val applicationReference = memberService.applyForEvent(applyForEventCommand);
        communicationService.sendApplicationConfirmationEmail(testContactDetails.getEmail(), eventCode);
        return ResponseEntity.ok(applicationReference);
    }

    @GetMapping(value = "/{applicationReference}/check-in-test")
    public ResponseEntity<String> testCheckIn(@PathVariable String applicationReference) throws Exception {
        val command = CheckInCommand.builder()
                .applicationReference(applicationReference)
                .position(new Coordinates(new BigDecimal("12.5"), new BigDecimal("55.7")))
                .build();

        command.setApplicationReference(applicationReference);
        memberService.checkIn(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{applicationReference}/decline-test")
    public ResponseEntity<String> testDecline(@PathVariable String applicationReference) {
        memberService.declineApplication(applicationReference);
        return ResponseEntity.ok().build();
    }
}
