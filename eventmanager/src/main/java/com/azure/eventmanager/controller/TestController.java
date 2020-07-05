package com.azure.eventmanager.controller;

import com.azure.eventmanager.domain.Coordinates;
import com.azure.eventmanager.domain.MemberEntity;
import com.azure.eventmanager.service.CommandMapper;
import com.azure.eventmanager.service.CommunicationService;
import com.azure.eventmanager.service.EventManagerService;
import com.azure.eventmanager.service.MemberService;
import com.azure.eventmanager.vo.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController(value = "test")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class TestController {

    private final MemberService memberService;
    private final EventManagerService eventManagerService;

    private static final String organizer = "organizer@organizer.com";
    private static final String member = "member-1";

    @GetMapping(value = "/prepare-test")
    public ResponseEntity<String> testPrepare() {
        val registerEventCommand = RegisterEventCommand.builder()
                .address("Riga Nicgales 38")
                .name("Event - 1")
                .eventStart(LocalDateTime.now().plusHours(2))
                .eventEnd(LocalDateTime.now().plusHours(5))
                .organizer("Org-1")
                .totalSpots(20)
                .platform("Platform-1")
                .rate(new BigDecimal("0.7"))
                .organizerUsername(organizer)
                .build();
        val code = eventManagerService.registerEvent(registerEventCommand);
        return ResponseEntity.ok(code);
    }

    @GetMapping(value = "/apply-test/{eventCode}")
    public ResponseEntity<String> testApply(@PathVariable String eventCode) {

        val applyForEventCommand = ApplyCommand.builder()
                .eventCode(eventCode)
                .memberReference(member)
                .contactDetails(new ContactDetails("email1@email.lv", "123456"))
                .build();

        val applicationReference = memberService.applyForEvent(applyForEventCommand);
        return ResponseEntity.ok(applicationReference);
    }

    @GetMapping(value = "/{applicationReference}/check-in-test")
    public ResponseEntity<String> testCheckIn(@PathVariable String applicationReference) {
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

    @GetMapping(value = "/{memberReference}/details-test")
    public ResponseEntity<MemberEntity> testGetMemberDetails(@PathVariable String memberReference) {
        val member = memberService.getMemberDetails(memberReference);
        return ResponseEntity.ok(member);
    }

    @PostMapping(value = "/register-organizer-test")
    public ResponseEntity<Void> testRegisterOrganizer(@RequestBody RegisterOrganizerRequest request) {
        eventManagerService.saveOrUpdateOrganizerName(request.getOrganizerUsername(), request.getOrganizerName());
        return ResponseEntity.ok().build();
    }
}
