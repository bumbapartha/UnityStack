package io.unitystack.platform.inception;

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
    public InceptionService.TurnResponse turn(@RequestBody InceptionService.TurnRequest request) {
        return service.turn(request);
    }
}
