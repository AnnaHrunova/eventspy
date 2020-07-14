package com.azure.eventmanager.controller;

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
import com.azure.eventmanager.service.EventManagerService;
import com.azure.eventmanager.service.MlService;

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
    private final MlService mlService;

    @PreAuthorize("hasRole('users')")
    @PostMapping(value = "/register")
    public ModelAndView registerEvent(@Valid @ModelAttribute(EventSpy.Model.EVENT) RegisterEventRequest request,
                                      HttpServletRequest httpServletRequest) {
        val organizer = httpServletRequest.getUserPrincipal().getName();

        val command = mapper.map(request);
        command.setOrganizer(organizer);
        eventManagerService.registerEvent(command);
        return new ModelAndView(String.format("redirect:%s", request.getOrganizer()));
    }

    @PreAuthorize("hasRole('users')")
    @GetMapping(value = "/register")
    public ModelAndView initRegisterEvent(HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView(EventSpy.View.ADD_EVENT);
        val organizer = httpServletRequest.getUserPrincipal().getName();
        val request = new RegisterEventRequest();
        request.setOrganizer(organizer);
        modelAndView.addObject(EventSpy.Model.EVENT, request);
        return modelAndView;
    }

    @PreAuthorize("hasRole('users')")
    @GetMapping(value = "/all")
    public ModelAndView showAllOrganizerEvents(HttpServletRequest request) {
        val username = request.getUserPrincipal().getName();
        val events = eventManagerService.findEvents(username);
//        mlService.sendRequest();
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
