package yoon.docker.mapService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yoon.docker.mapService.service.MapService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/maps")
public class MapController {

    private final MapService mapService;

}
