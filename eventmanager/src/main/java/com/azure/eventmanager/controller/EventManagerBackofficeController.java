package com.azure.eventmanager.controller;

import static org.apache.logging.log4j.util.Strings.isBlank;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.azure.eventmanager.service.CommandMapper;
import com.azure.eventmanager.service.CommunicationService;
import com.azure.eventmanager.service.EventManagerService;
import com.azure.eventmanager.service.MlService;
import com.azure.eventmanager.vo.EmailCommunication;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@RequestMapping(value = "events")
@Slf4j
@AllArgsConstructor
@Controller
public class EventManagerBackofficeController {

    private final EventManagerService eventManagerService;
    private final CommandMapper mapper;
    private final CommunicationService communicationService;
    private final MlService mlService;

    @PreAuthorize("hasRole('users')")
    @PostMapping(value = "/register")
    public ModelAndView registerEvent(@Valid @ModelAttribute(EventSpy.Model.EVENT) RegisterEventRequest request,
                                      HttpServletRequest httpServletRequest) {
        val username = httpServletRequest.getUserPrincipal().getName();
        val organizer = eventManagerService.getOrganizerName(username);
        val command = mapper.map(request);
        command.setOrganizerUsername(username);
        command.setOrganizer(organizer);
        eventManagerService.registerEvent(command);
        return new ModelAndView(String.format("redirect:events/%s/%s", request.getPlatform(), request.getOrganizer()));
    }

    @PreAuthorize("hasRole('users')")
    @GetMapping(value = "/register")
    public ModelAndView initRegisterEvent(HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView(EventSpy.View.ADD_EVENT);
        val username = httpServletRequest.getUserPrincipal().getName();
        String organizer = eventManagerService.getOrganizerName(username);
        if (isBlank(organizer)) {
            organizer = "testorg1";
            eventManagerService.saveOrUpdateOrganizerName(username, organizer);
        }
        val request = new RegisterEventRequest();
        request.setOrganizer(organizer);
        modelAndView.addObject(EventSpy.Model.EVENT, request);

        EmailCommunication communication = new EmailCommunication();
        communication.setTo("anna.hrunova93@gmail.com");
        communication.setSubject("Thank You For Application");
        communication.setContent(String.format("You have just applied for the event: %s.", 123) +
                String.format("To decline your application, please follow the link: %s", 123));
        communication.setType("application.link");
        communicationService.sendEmail(communication);
        return modelAndView;
    }

    @PreAuthorize("hasRole('users')")
    @GetMapping(value = "/all")
    public ModelAndView showAllOrganizerEvents(HttpServletRequest request) {
        val username = request.getUserPrincipal().getName();
        val events = eventManagerService.findEvents(username);
        mlService.sendRequest();
        final ModelAndView modelAndView = new ModelAndView(EventSpy.View.EVENTS);
        modelAndView.addObject(EventSpy.Model.EVENTS, events);
        return modelAndView;
    }

    @PreAuthorize("hasRole('users')")
    @GetMapping(value = "/details/{code}")
    public ModelAndView getEventDetails(@PathVariable String code) {
        val event = eventManagerService.findByCode(code);
        final ModelAndView modelAndView = new ModelAndView(EventSpy.View.EVENT_DETAILS);
        modelAndView.addObject(EventSpy.Model.EVENT_DETAILS, event);
        return modelAndView;
    }
}
