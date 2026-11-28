package io.unitystack.platform.requirements.api;

import io.unitystack.platform.requirements.application.InceptionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inception")
public class InceptionController {
    private final InceptionService service;

    public InceptionController(InceptionService service) {
        this.service = service;
    }

    @PostMapping("/turn")
    public InceptionTurnResponse turn(@RequestBody InceptionTurnRequest request) {
        return service.turn(request);
    }
}
