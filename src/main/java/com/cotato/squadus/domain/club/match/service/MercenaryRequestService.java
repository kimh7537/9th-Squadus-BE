package com.cotato.squadus.domain.club.match.service;

import com.cotato.squadus.api.mercenary.dto.response.MercenaryRequestAndMercenaryPostResponse;
import com.cotato.squadus.api.mercenary.dto.response.MercenaryRequestResponse;
import com.cotato.squadus.api.mercenary.dto.response.ReceivedMercenaryRequestResponse;
import com.cotato.squadus.common.error.ErrorCode;
import com.cotato.squadus.common.error.exception.AppException;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.repository.ClubAdminMemberRepository;
import com.cotato.squadus.domain.club.common.repository.ClubRepository;
import com.cotato.squadus.domain.club.match.entity.MercenaryPost;
import com.cotato.squadus.domain.club.match.entity.MercenaryRequest;
import com.cotato.squadus.domain.club.match.repository.MercenaryPostRepository;
import com.cotato.squadus.domain.club.match.repository.MercenaryRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MercenaryRequestService {

    private final MercenaryRequestRepository mercenaryRequestRepository;
    private final ClubAdminMemberRepository clubAdminMemberRepository;
    private final ClubRepository clubRepository;
    private final MercenaryPostRepository mercenaryPostRepository;

    public Page<MercenaryRequestResponse> getMyRequests(Long memberId, Pageable pageable) {
        return mercenaryRequestRepository.findAllByClubMember_ClubMemberIdx(memberId, pageable)
                .map(MercenaryRequestResponse::from);
    }


    public List<MercenaryRequestResponse> getAllMyRequests(Long memberId) {
        return mercenaryRequestRepository.findAllByClubMember_ClubMemberIdx(memberId)
                .stream()
                .map(MercenaryRequestResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelMatchRequest(Long requestId, Long memberId) {
        MercenaryRequest mercenaryRequest = mercenaryRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 요청을 찾을 수 없습니다."));

        // 요청을 취소하려는 사용자가 이 요청을 만든 사용자인지 확인
        if (!mercenaryRequest.getClubMember().getClubMemberIdx().equals(memberId)) {
            throw new AppException(ErrorCode.CLUB_ACCESS_DENIED);
        }

        mercenaryRequestRepository.delete(mercenaryRequest);
    }



    public Page<MercenaryRequestAndMercenaryPostResponse> getReceivedMatchRequests(Long clubId, Pageable pageable) {

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("동아리를 찾을 수 없습니다."));

        List<MercenaryPost> mercenaryPosts = mercenaryPostRepository.findByHomeClub(club);

        List<MercenaryRequestAndMercenaryPostResponse> allResponses = mercenaryPosts.stream()
                .flatMap(mercenaryPost -> {
                    List<ReceivedMercenaryRequestResponse> receivedRequests = mercenaryPost.getMercenaryRequests()
                            .stream()
                            .map(ReceivedMercenaryRequestResponse::from)
                            .collect(Collectors.toList());

                    return mercenaryPost.getMercenaryRequests().stream()
                            .map(mercenaryRequest -> MercenaryRequestAndMercenaryPostResponse.from(mercenaryRequest, receivedRequests));
                })
                .collect(Collectors.toList());

        // allResponses 리스트를 pageable에 맞게 자름
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allResponses.size());

        List<MercenaryRequestAndMercenaryPostResponse> paginatedList = allResponses.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, allResponses.size());
    }


    public List<MercenaryRequestAndMercenaryPostResponse> getAllReceivedMatchRequests(Long clubId) {

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("동아리를 찾을 수 없습니다."));

        List<MercenaryPost> mercenaryPosts = club.getMercenaryPosts();

        return mercenaryPosts.stream()
                .flatMap(mercenaryPost -> {
                    List<ReceivedMercenaryRequestResponse> receivedRequests = mercenaryPost.getMercenaryRequests()
                            .stream()
                            .map(ReceivedMercenaryRequestResponse::from)
                            .collect(Collectors.toList());

                    return mercenaryPost.getMercenaryRequests().stream()
                            .map(mercenaryRequest -> MercenaryRequestAndMercenaryPostResponse.from(mercenaryRequest, receivedRequests)
                            );
                }).collect(Collectors.toList());
    }




    @Transactional
    public void decideMatchRequest(Long requestId, String decision, Long memberId) {
        MercenaryRequest mercenaryRequest = mercenaryRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 요청을 찾을 수 없습니다."));

        // 해당 용병 요청이 속한 Club의 Admin 권한 확인
        clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(
                        mercenaryRequest.getMercenaryPost().getHomeClub().getClubId(), memberId)
                .orElseThrow(() -> new AppException(ErrorCode.CLUB_ACCESS_DENIED));


        if ("ACCEPTED".equalsIgnoreCase(decision)) {
            mercenaryRequest.accept();
            mercenaryRequest.getMercenaryPost().incrementParticipants(); // 현재 인원 증가
        } else if ("REJECTED".equalsIgnoreCase(decision)) {
            mercenaryRequest.reject();
        } else {
            throw new IllegalArgumentException("결정은 ACCEPTED 또는 REJECTED이어야 합니다.");
        }

        mercenaryRequestRepository.save(mercenaryRequest);
    }
}
