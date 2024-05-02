package yoon.docker.mapService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yoon.docker.mapService.dto.request.MapDto;
import yoon.docker.mapService.dto.response.MapMemberResponse;
import yoon.docker.mapService.dto.response.MapResponse;
import yoon.docker.mapService.dto.response.MemberResponse;
import yoon.docker.mapService.service.MapService;
import yoon.docker.mapService.dto.request.AddedMemberDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/maps")
public class MapController {

    private final MapService mapService;

    //지도 불러오기
    @GetMapping()
    public ResponseEntity<List<MapResponse>> getMap(){

        List<MapResponse> result = mapService.getMapList();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //지도 멤버 보기
    @GetMapping("/{mapIdx}/members")
    public ResponseEntity<List<MemberResponse>> getMember(@PathVariable long mapIdx){

        List<MemberResponse> result = mapService.getMemberList(mapIdx);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //지도 만들기
    @PostMapping()
    public ResponseEntity<MapResponse> postMap(@RequestBody MapDto dto){

        MapResponse result = mapService.createNewMap(dto);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


    //지도에 멤버 초대
    @PostMapping("/{mapIdx}/members")
    public ResponseEntity<List<MapMemberResponse>> addMember(@PathVariable long mapIdx, @RequestBody List<AddedMemberDto> dto){

        List<MapMemberResponse> result = mapService.addMembers(mapIdx, dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    //지도에 새멤버 초대
    @PostMapping("/{mapIdx}/members")
    public ResponseEntity<MapMemberResponse> addMember(@PathVariable long mapIdx, @RequestBody AddedMemberDto dto){

        MapMemberResponse result = mapService.addNewMember(mapIdx, dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //지도 이름 바꾸기
    @PutMapping("/{mapIdx}/title")
    public ResponseEntity<?> changeTitle(@PathVariable long mapIdx, @RequestBody MapDto dto){

        MapResponse result = mapService.changeMapTitle(mapIdx, dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //지도 나가기
    @DeleteMapping("/{mapIdx}")
    public ResponseEntity<?> exitMap(@PathVariable long mapIdx){

        mapService.exitCurrentMap(mapIdx);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

}
