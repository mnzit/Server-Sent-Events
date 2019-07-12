/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mnzit.sse.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 *
 * @author Mnzit
 */
@RestController
public class SSEController {
    
    private List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    
    @RequestMapping("/questions")
    public SseEmitter questions() {
        SseEmitter sseEmitter = new SseEmitter(1500000L);
        emitters.add(sseEmitter);
        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));
        return sseEmitter;
    }
    
    @RequestMapping(value = "/new-question", method = RequestMethod.POST)
    public void postQuestion(String question) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("spring").data(question));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
