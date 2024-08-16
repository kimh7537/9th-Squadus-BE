package com.cotato.squadus.domain.club.match.service;

import com.cotato.squadus.api.match.dto.match.response.MatchRequestAndMatchPostResponse;
import com.cotato.squadus.api.match.dto.match.response.MatchRequestResponse;
import com.cotato.squadus.api.match.dto.match.response.ReceivedMatchRequestResponse;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.common.repository.ClubAdminMemberRepository;
import com.cotato.squadus.domain.club.common.repository.ClubRepository;
import com.cotato.squadus.domain.club.match.entity.MatchPost;
import com.cotato.squadus.domain.club.match.entity.MatchRequest;
import com.cotato.squadus.domain.club.match.repository.MatchPostRepository;
import com.cotato.squadus.domain.club.match.repository.MatchRequestRepository;
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
public class MatchRequestService {

    private final MatchRequestRepository matchRequestRepository;
    private final ClubAdminMemberRepository clubAdminMemberRepository;
    private final ClubRepository clubRepository;
    private final MatchPostRepository matchPostRepository;

    public Page<MatchRequestResponse> getMyClubMatchRequests(Long clubId, Pageable pageable) {
        return matchRequestRepository.findAllByClub_ClubId(clubId, pageable)
                .map(MatchRequestResponse::from);
    }


    public List<MatchRequestResponse> getAllMyClubMatchRequests(Long clubId) {
        return matchRequestRepository.findAllByClub_ClubId(clubId)
                .stream()
                .map(MatchRequestResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelMatchRequest(Long requestId, Long memberId) {
        MatchRequest matchRequest = matchRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 요청을 찾을 수 없습니다."));

        clubAdminMemberRepository.findActiveAdminByClubIdAndMemberId(
                        matchRequest.getClub().getClubId(), memberId)
                .orElseThrow(() -> new AccessDeniedException("매칭 요청을 취소할 권한이 없습니다."));

//        // 해당 club의 임원인지 확인
//        if (!clubAdminMemberRepository.findActiveAdminByClubIdAndMemberId( matchRequest.getClub().getClubId(), memberId).isPresent()) {
//            throw new AccessDeniedException("매칭 요청을 보낼 권한이 없습니다.");
//        }

        matchRequestRepository.delete(matchRequest);
    }




    public Page<MatchRequestAndMatchPostResponse> getReceivedMatchRequests(Long clubId, Pageable pageable) {

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("동아리를 찾을 수 없습니다."));

        List<MatchPost> matchPosts = matchPostRepository.findByHomeClub(club);

        List<MatchRequestAndMatchPostResponse> allResponses = matchPosts.stream()
                .flatMap(matchPost -> {
                    List<ReceivedMatchRequestResponse> receivedRequests = matchPost.getMatchRequests()
                            .stream()
                            .map(ReceivedMatchRequestResponse::from)
                            .collect(Collectors.toList());

                    return matchPost.getMatchRequests().stream()
                            .map(matchRequest -> MatchRequestAndMatchPostResponse.from(matchRequest, receivedRequests));
                })
                .collect(Collectors.toList());

        // allResponses 리스트를 pageable에 맞게 자릅니다.
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allResponses.size());

        List<MatchRequestAndMatchPostResponse> paginatedList = allResponses.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, allResponses.size());
    }


    public List<MatchRequestAndMatchPostResponse> getAllReceivedMatchRequests(Long clubId) {

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("동아리를 찾을 수 없습니다."));

        List<MatchPost> matchPosts = club.getMatchPosts();

        return matchPosts.stream()
                .flatMap(matchPost -> {

//                    club.addMatchPost(matchPost);

                     List<ReceivedMatchRequestResponse> receivedRequests = matchPost.getMatchRequests()
                             .stream()
                             .map(ReceivedMatchRequestResponse::from)
                             .collect(Collectors.toList());

            return matchPost.getMatchRequests().stream()
                    .map(matchRequest -> MatchRequestAndMatchPostResponse.from(matchRequest, receivedRequests)
            );
        }).collect(Collectors.toList());
    }




    @Transactional
    public void decideMatchRequest(Long requestId, String decision, Long memberId) {
        MatchRequest matchRequest = matchRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 요청을 찾을 수 없습니다."));

        clubAdminMemberRepository.findActiveAdminByClubIdAndMemberId(
                        matchRequest.getClub().getClubId(), memberId)
                .orElseThrow(() -> new AccessDeniedException("매칭 요청에 대해 결정할 권한이 없습니다."));

        if ("ACCEPTED".equalsIgnoreCase(decision)) {
            matchRequest.accept();
        } else if ("REJECTED".equalsIgnoreCase(decision)) {
            matchRequest.reject();
        } else {
            throw new IllegalArgumentException("결정은 ACCEPTED 또는 REJECTED이어야 합니다.");
        }

        matchRequestRepository.save(matchRequest);
    }
}

