package yoon.docker.mapService.service;

import jakarta.persistence.LockTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionTimedOutException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import yoon.docker.mapService.dto.request.AddedMemberDto;
import yoon.docker.mapService.dto.request.MapDto;
import yoon.docker.mapService.dto.response.MapMemberResponse;
import yoon.docker.mapService.dto.response.MapResponse;
import yoon.docker.mapService.dto.response.MemberResponse;
import yoon.docker.mapService.entity.MapMembers;
import yoon.docker.mapService.entity.Maps;
import yoon.docker.mapService.entity.Members;
import yoon.docker.mapService.entity.PrivateMap;
import yoon.docker.mapService.enums.ExceptionCode;
import yoon.docker.mapService.exception.MapException;
import yoon.docker.mapService.exception.PessimisticLockTimeOutException;
import yoon.docker.mapService.exception.UnAuthorizedException;
import yoon.docker.mapService.repository.MapMemberRepository;
import yoon.docker.mapService.repository.MapRepository;
import yoon.docker.mapService.repository.MemberRepository;
import yoon.docker.mapService.repository.PrivateMapRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService {

    private final RestTemplate restTemplate;

    private final MapRepository mapRepository;

    private final MemberRepository memberRepository;

    private final MapMemberRepository mapMemberRepository;

    private final PrivateMapRepository privateMapRepository;


    private MapResponse toResponse(Maps maps){return new MapResponse(maps.getMapIdx(), maps.getTitle(),
             maps.getCreatedAt(), maps.getUpdatedAt());}
    private MemberResponse toResponse(Members members){return new MemberResponse(members.getMemberIdx(),
            members.getEmail(), members.getUsername(), members.getProfile(), members.getCreatedAt(), members.getUpdatedAt());}
    private MapMemberResponse toResponse(MapMembers mapMembers){return new MapMemberResponse(mapMembers.getMapMembersIdx(),
            mapMembers.getMembers().getUsername(), mapMembers.getMaps().getTitle(), mapMembers.getCreatedAt());}
    private MapResponse toResponse(PrivateMap privateMap){return new MapResponse(privateMap.getPrivateMapIdx(),
            null, null, privateMap.getUpdatedAt());}


    //지도 불러오기
    @Transactional(readOnly = true)
    public List<MapResponse> getMapList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨

        Members currentMember = (Members) authentication.getPrincipal();

        List<Maps> list = mapRepository.findAllByMapMembers_Members(currentMember);
        List<MapResponse> response = new ArrayList<>();

        for(Maps map: list){
            response.add(toResponse(map));
        }

        return response;
    }


    //지도 멤버 보기
    @Transactional(readOnly = true)
    public List<MemberResponse> getMemberList(long mapIdx){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨

        Members currentMember = (Members) authentication.getPrincipal();
        Maps currentMap = mapRepository.findMapsByMapIdx(mapIdx);

        if(currentMap == null)
            throw new MapException(ExceptionCode.MAP_NOT_FOUND.getMessage(), ExceptionCode.MAP_NOT_FOUND.getStatus());           //Map Exception
        if(!mapMemberRepository.existsByMapsAndMembers(currentMap, currentMember))//해당 지도의 회원이 아님
            throw new MapException(ExceptionCode.NOT_MAP_USER.getMessage(), ExceptionCode.NOT_MAP_USER.getStatus());


        List<MapMembers> list = mapMemberRepository.findAllByMaps(currentMap);
        List<MemberResponse> responses = new ArrayList<>();

        for(MapMembers mm : list){
            responses.add(toResponse(mm.getMembers()));
        }

        return responses;
    }


    //지도 만들기
    @Transactional
    public MapResponse createNewMap(MapDto dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨

        Members currentMember = (Members) authentication.getPrincipal();
        Maps newMap = Maps.builder()
                .title(dto.getTitle())
                .build();
        MapMembers mapMembers = MapMembers.builder()
                .members(currentMember)
                .maps(newMap)
                .build();

        mapMemberRepository.save(mapMembers);
        return toResponse(mapRepository.save(newMap));
    }

    //개인지도 만들기
    @Transactional// 호출한 외부 서비스에서도 롤백 필요
    public MapResponse createPrivateMap(long memberIdx){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨

        Members currentMember = (Members) authentication.getPrincipal();
        if(currentMember.getMemberIdx() != memberIdx)
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus());

        PrivateMap privateMap = new PrivateMap(currentMember);

        return toResponse(privateMapRepository.save(privateMap));
    }


    //멤버 초대
    @Transactional
    public List<MapMemberResponse> addMembers(long mapIdx, List<AddedMemberDto> list){
        Maps currentMap = mapRepository.findMapsByMapIdx(mapIdx);
        List<MapMemberResponse> responses = new ArrayList<>();
        if(currentMap == null)
            throw new MapException(ExceptionCode.MAP_NOT_FOUND.getMessage(), ExceptionCode.MAP_NOT_FOUND.getStatus()); //MapException
        for(AddedMemberDto dto : list){
            Members addedMember = memberRepository.findMembersByMemberIdx(dto.getIdx());
            if(addedMember == null)
                continue;
            MapMembers mm = MapMembers.builder()
                    .members(addedMember)
                    .maps(currentMap)
                    .build();
            if(!mapMemberRepository.existsByMapsAndMembers(currentMap, addedMember))
                responses.add(toResponse(mapMemberRepository.save(mm)));
        }

        return responses;
    }

    //새로운 멤버 초대
    @Transactional
    public MapMemberResponse addNewMember(long mapIdx, AddedMemberDto dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨

        Members currentMember = (Members) authentication.getPrincipal();
        Members newMember = memberRepository.findMembersByMemberIdx(dto.getIdx());
        Maps currentMap = mapRepository.findMapsByMapIdx(mapIdx);

        if(newMember == null)
            throw new UsernameNotFoundException(String.valueOf(dto.getIdx()));  //초대하는 유저가 존재하지 않음
        if(currentMap == null)
            throw new MapException(ExceptionCode.MAP_NOT_FOUND.getMessage(), ExceptionCode.MAP_NOT_FOUND.getStatus());   //MapException 지도가 존재하지 않음

        try {       //Lock Pessimistic.Write  해당 유저가 회원인지 확인(중복 가입 방지)
            if (!mapMemberRepository.existsByMapsAndMembers(currentMap, currentMember))//MapException  유저가 지도의 회원이 아님
                throw new MapException(ExceptionCode.NOT_MAP_USER.getMessage(), ExceptionCode.NOT_MAP_USER.getStatus());
        }catch (LockTimeoutException | TransactionTimedOutException e) {
            throw new PessimisticLockTimeOutException(ExceptionCode.LOCK_TIMEOUT_ERROR.getMessage(),
                    ExceptionCode.LOCK_TIMEOUT_ERROR.getStatus()); //Lock 에외
        }

        MapMembers mm = MapMembers.builder()
                .maps(currentMap)
                .members(newMember)
                .build();
        if(mapMemberRepository.existsByMapsAndMembers(currentMap, newMember))//MapException 해당 유저가 이미 회원
            throw new MapException(ExceptionCode.ALREADY_MAP_USER.getMessage(), ExceptionCode.ALREADY_MAP_USER.getStatus());

        return toResponse(mapMemberRepository.save(mm));
    }
    //지도 이름 바꾸기
    @Transactional
    public MapResponse changeMapTitle(long mapIdx, MapDto dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨

        Members currentMember = (Members) authentication.getPrincipal();
        Maps currentMap = mapRepository.findMapsByMapIdx(mapIdx);
        if(currentMap == null)//해당 지도가 존재하지 않음
            throw new MapException(ExceptionCode.MAP_NOT_FOUND.getMessage(), ExceptionCode.MAP_NOT_FOUND.getStatus());
        if(!mapMemberRepository.existsByMapsAndMembers(currentMap, currentMember))//지도에 권한이 없음 (회원이 아님)
            throw new MapException(ExceptionCode.NOT_MAP_USER.getMessage(),
                    ExceptionCode.NOT_MAP_USER.getStatus());

        currentMap.setTitle(dto.getTitle());

        return toResponse(mapRepository.save(currentMap));
    }


    //지도 나가기
    @Transactional
    public void exitCurrentMap(long mapIdx){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨

        Members currentMember = (Members) authentication.getPrincipal();
        Maps currentMap = mapRepository.findMapsByMapIdx(mapIdx);
        if(currentMap == null)//해당 지도가 존재하지 않음
            throw new MapException(ExceptionCode.MAP_NOT_FOUND.getMessage(), ExceptionCode.MAP_NOT_FOUND.getStatus());
        if(!mapMemberRepository.existsByMapsAndMembers(currentMap, currentMember))//지도에 권한이 없음 (회원이 아님)
            throw new MapException(ExceptionCode.NOT_MAP_USER.getMessage(), ExceptionCode.NOT_MAP_USER.getStatus());

        MapMembers mm = mapMemberRepository.findMapMembersByMapsAndMembers(currentMap, currentMember);

        mapMemberRepository.delete(mm);

        if(!mapMemberRepository.existsByMaps(currentMap))       //만약 회원이 다 나갔을 경우 지도도 삭제
            mapRepository.delete(currentMap);
    }

}
