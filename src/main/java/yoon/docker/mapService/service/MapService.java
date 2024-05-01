package yoon.docker.mapService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yoon.docker.mapService.repository.MapRepository;

@Service
@RequiredArgsConstructor
public class MapService {

    private final MapRepository mapRepository;




}
