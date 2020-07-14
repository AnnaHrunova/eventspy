package com.azure.eventmanager.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.eventmanager.service.CommandMapper;
import com.azure.eventmanager.service.CommunicationService;
import com.azure.eventmanager.service.EventManagerService;
import com.azure.eventmanager.service.MemberService;
import com.azure.eventmanager.service.StatisticsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

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
        memberService.applyForEvent(command);
        communicationService.sendApplicationConfirmationEmail(request.getEmail(), request.getEventCode());
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

    @GetMapping(value = "/member-statistics/{memberId}/{email}/{phone}")
    public ResponseEntity<MemberStatisticsResponse> getUserStatistics(@PathVariable String memberId,
                                                                      @PathVariable String email,
                                                                      @PathVariable String phone) {
        val response = memberService.getMemberDetails(memberId);

        response.setRateByEmail(statisticsService.getEmailStatistics(email).getStatistics().getRate());
        response.setRateByPhone(statisticsService.getPhoneStatistics(phone).getStatistics().getRate());
        return ResponseEntity.ok(response);
    }
}
