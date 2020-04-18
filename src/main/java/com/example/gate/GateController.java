package com.example.gate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class GateController {

    final
    GateRepository gateRepository;

    public GateController(GateRepository gateRepository) {
        this.gateRepository = gateRepository;
    }

    @GetMapping("/passed")
    Gate processPassed(@RequestParam(value = "gate") Long gateId, @RequestParam(value = "employee") String employeeName) {
        Optional<Gate> gateOptional = gateRepository.findById(gateId);
        Gate gate = gateOptional.get();
        eventPublish(gate, employeeName);
        return gate;
    }

    void eventPublish(Gate gate, String employeeName) {
        EmployeePassed employeePassed = new EmployeePassed();
        employeePassed.setFrom(gate.getFromA());
        employeePassed.setTo(gate.getToB());
        employeePassed.setEmployeeName(employeeName);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(employeePassed);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        Processor processor = GateApplication.applicationContext.getBean(Processor.class);
        MessageChannel outputChannel = processor.output();

        outputChannel.send(MessageBuilder.withPayload(json).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
    }

}
