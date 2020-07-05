package com.azure.eventmanager.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class EventManagerCheckInController {

    @GetMapping(value = "/{applicationReference}/check-in")
    public ModelAndView getCheckIn(@PathVariable String applicationReference) {
        ModelAndView modelAndView = new ModelAndView(EventSpy.View.CHECK_IN);
        modelAndView.addObject(EventSpy.Model.APPLICATION_REFERENCE, applicationReference);
        return modelAndView;
    }
}
