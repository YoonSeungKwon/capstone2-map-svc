package yoon.docker.mapService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yoon.docker.mapService.dto.request.MapDto;
import yoon.docker.mapService.dto.response.MapMemberResponse;
import yoon.docker.mapService.dto.response.MapResponse;
import yoon.docker.mapService.dto.response.MemberResponse;
import yoon.docker.mapService.service.MapService;
import yoon.docker.mapService.dto.request.AddedMemberDto;

import java.util.List;

@Tag(name="지도관련 API", description = "version1")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/maps")
public class MapController {

    private final MapService mapService;


    //지도 불러오기
    @Operation(summary = "지도 불러오기", description = "가입 되어있는 지도들을 리스트로 불러온다.")
    @GetMapping()
    public ResponseEntity<List<MapResponse>> getPrivateMap(){

        List<MapResponse> result = mapService.getPrivateMapList();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "지도 불러오기", description = "가입 되어있는 지도들을 리스트로 불러온다.")
    @GetMapping()
    public ResponseEntity<List<MapResponse>> getSharedMap(){

        List<MapResponse> result = mapService.getSharedMapList();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //지도 멤버 보기
    @Operation(summary = "지도 멤버 보기", description = "해당 지도에 가입되어있는 멤버들을 리스트로 불러온다.")
    @GetMapping("/members/{mapIdx}")
    public ResponseEntity<List<MemberResponse>> getMember(@PathVariable long mapIdx){

        List<MemberResponse> result = mapService.getMemberList(mapIdx);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //지도 만들기
    @Operation(summary = "새로운 지도 만들기", description = "새로운 지도 만들기")
    @PostMapping()
    public ResponseEntity<MapResponse> postMap(@RequestBody @Validated MapDto dto){

        MapResponse result = mapService.createNewMap(dto);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    //개인 지도 만들기(유저당 하나 회원가입시 생성)
    @Operation(summary = "개인 지도 만들기", description = "회원가입시 주어지는 홈화면의 멤버 개인 지도 만들기")
    @PostMapping("/private")
    public ResponseEntity<MapResponse> postPrivateMap(@RequestBody @Validated MapDto dto){

        MapResponse result = mapService.createPrivateMap(dto);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    //지도에 멤버 초대
    @Operation(summary = "지도 만들면서 멤버 추가하기", description = "지도를 제작 후 바로 멤버 추가하는 기능")
    @PostMapping("/members/{mapIdx}")
    public ResponseEntity<List<MapMemberResponse>> addMember(@PathVariable long mapIdx, @RequestBody List<AddedMemberDto> dto){

        List<MapMemberResponse> result = mapService.addMembers(mapIdx, dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    //지도에 새멤버 초대
    @Operation(summary = "기존 지도에 새로운 멤버 초대하기", description = "원래 있던 지도에 추가로 멤버를 초대하는 기능")
    @PostMapping("/adds")
    public ResponseEntity<MapMemberResponse> addNewMember(@RequestBody @Validated AddedMemberDto dto){

        MapMemberResponse result = mapService.addNewMember(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //지도 이름 바꾸기
    @Operation(summary = "지도의 이름 바꾸기", description = "해당 지도의 이름 바꾸기")
    @PutMapping("/title/{mapIdx}")
    public ResponseEntity<?> changeTitle(@PathVariable long mapIdx, @RequestBody @Validated MapDto dto){

        MapResponse result = mapService.changeMapTitle(mapIdx, dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //지도 나가기
    @Operation(summary = "해당 지도 나가기", description = "해당 지도의 회원에서 나가기")
    @DeleteMapping("/{mapIdx}")
    public ResponseEntity<?> exitMap(@PathVariable long mapIdx){

        mapService.exitCurrentMap(mapIdx);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

}
