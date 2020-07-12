package com.azure.eventmanager.controller;

import com.azure.eventmanager.service.*;
import com.azure.eventmanager.vo.EmailCommunication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class EventManagerApiController {

    private final MemberService memberService;
    private final EventManagerService eventManagerService;
    private final CommunicationService communicationService;
    private final CommandMapper mapper;
    private final StatisticsService statisticsService;

    @PostMapping(value = "/apply", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> apply(@RequestBody ApplyRequest request) {
        val command = mapper.map(request);
        val applicationReference = memberService.applyForEvent(command);
        val declineLink = String.format("http://localhost:8080/%s/decline", applicationReference);

        EmailCommunication communication = new EmailCommunication();
        communication.setTo("anna.hrunova93@gmail.com");
        communication.setSubject("Thank You For Application");
        communication.setContent(String.format("You have just applied for the event: %s.", request.getEventCode()) +
                String.format("To decline your application, please follow the link: %s", declineLink));
        communication.setType("application.link");
        communicationService.sendEmail(communication);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{applicationReference}/check-in", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkIn(@PathVariable String applicationReference,
                                          @RequestBody CheckInRequest request) throws Exception {
        val command = mapper.map(request);
        command.setApplicationReference(applicationReference);
        memberService.checkIn(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{applicationReference}/decline")
    public ResponseEntity<String> decline(@PathVariable String applicationReference) {
        memberService.declineApplication(applicationReference);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/register-organizer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registerOrganizer(@RequestBody RegisterOrganizerRequest request) {
        eventManagerService.saveOrUpdateOrganizerName(request.getOrganizerUsername(), request.getOrganizerName());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/member-statistics/{memberId}")
    public ResponseEntity<MemberStatisticsResponse> getUserStatistics(@PathVariable String memberId) {
        val contactDetails = memberService.getMemberContactDetails(memberId);
        val response = memberService.getMemberDetails(memberId);

        response.setRateByEmail(statisticsService.getEmailStatistics(contactDetails.getEmail()).getStatistics().getRate());
        response.setRateByPhone(statisticsService.getPhoneStatistics(contactDetails.getMobilePhone()).getStatistics().getRate());
        return ResponseEntity.ok(response);
    }
}
