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
import yoon.docker.mapService.enums.Category;
import yoon.docker.mapService.enums.ExceptionCode;
import yoon.docker.mapService.exception.PessimisticLockTimeOutException;
import yoon.docker.mapService.exception.UnAuthorizedException;
import yoon.docker.mapService.repository.MapMemberRepository;
import yoon.docker.mapService.repository.MapRepository;
import yoon.docker.mapService.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService {

    private final RestTemplate restTemplate;

    private final MapRepository mapRepository;

    private final MemberRepository memberRepository;

    private final MapMemberRepository mapMemberRepository;


    private MapResponse toResponse(Maps maps){return new MapResponse(maps.getMapIdx(), maps.getTitle(),
            maps.getCategory().getName(), maps.getCreatedAt(), maps.getUpdatedAt());}
    private MemberResponse toResponse(Members members){return new MemberResponse(members.getMemberIdx(),
            members.getEmail(), members.getUsername(), members.getProfile(), members.getCreatedAt(), members.getUpdatedAt());}
    private MapMemberResponse toResponse(MapMembers mapMembers){return new MapMemberResponse(mapMembers.getMapMembersIdx(),
            mapMembers.getMembers().getUsername(), mapMembers.getMaps().getTitle(), mapMembers.getCreatedAt());}

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
            throw new RuntimeException();           //Map Exception

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
                .category(Category.valueOf(dto.getCategory()))
                .build();
        MapMembers mapMembers = MapMembers.builder()
                .members(currentMember)
                .maps(newMap)
                .build();

        mapMemberRepository.save(mapMembers);
        return toResponse(mapRepository.save(newMap));
    }


    //멤버 초대
    @Transactional
    public List<MapMemberResponse> addMembers(long mapIdx, List<AddedMemberDto> list){
        Maps currentMap = mapRepository.findMapsByMapIdx(mapIdx);
        List<MapMemberResponse> responses = new ArrayList<>();
        if(currentMap == null)
            throw new RuntimeException(); //MapException
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
            throw new RuntimeException();   //MapException 지도가 존재하지 않음

        try {       //Lock Pessimistic.Write  해당 유저가 회원인지 확인(중복 가입 방지)
            if (!mapMemberRepository.existsByMapsAndMembers(currentMap, currentMember))
                throw new RuntimeException();      //UnAuthException  유저가 지도의 회원이 아님
        }catch (LockTimeoutException | TransactionTimedOutException e) {
            throw new PessimisticLockTimeOutException(ExceptionCode.LOCK_TIMEOUT_ERROR.getMessage(),
                    ExceptionCode.LOCK_TIMEOUT_ERROR.getStatus()); //Lock 에외
        }

        MapMembers mm = MapMembers.builder()
                .maps(currentMap)
                .members(newMember)
                .build();
        if(mapMemberRepository.existsByMapsAndMembers(currentMap, newMember))
            throw new RuntimeException();       //MapException 해당 유저가 이미 회원

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
        if(currentMap == null)
            throw new RuntimeException();       //해당 지도가 존재하지 않음
        if(!mapMemberRepository.existsByMapsAndMembers(currentMap, currentMember))
            throw new UnAuthorizedException(null, null); //지도에 권한이 없음 (회원이 아님)

        currentMap.setTitle(dto.getTitle());
        currentMap.setCategory(Category.valueOf(dto.getCategory()));

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
        if(currentMap == null)
            throw new RuntimeException();       //해당 지도가 존재하지 않음
        if(!mapMemberRepository.existsByMapsAndMembers(currentMap, currentMember))
            throw new UnAuthorizedException(null, null); //지도에 권한이 없음 (회원이 아님)

        MapMembers mm = mapMemberRepository.findMapMembersByMapsAndMembers(currentMap, currentMember);

        mapMemberRepository.delete(mm);

        if(!mapMemberRepository.existsByMaps(currentMap))       //만약 회원이 다 나갔을 경우 지도도 삭제
            mapRepository.delete(currentMap);
    }

}
