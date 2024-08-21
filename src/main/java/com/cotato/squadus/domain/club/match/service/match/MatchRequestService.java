package com.cotato.squadus.domain.club.match.service.match;

import com.cotato.squadus.api.match.dto.matchPost.response.MatchRequestAndMatchPostResponse;
import com.cotato.squadus.api.match.dto.matchPost.response.MatchRequestResponse;
import com.cotato.squadus.api.match.dto.matchPost.response.ReceivedMatchRequestResponse;
import com.cotato.squadus.common.error.ErrorCode;
import com.cotato.squadus.common.error.exception.AppException;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.repository.ClubAdminMemberRepository;
import com.cotato.squadus.domain.club.common.repository.ClubRepository;
import com.cotato.squadus.domain.club.match.entity.match.MatchPost;
import com.cotato.squadus.domain.club.match.entity.match.MatchRequest;
import com.cotato.squadus.domain.club.match.repository.match.MatchPostRepository;
import com.cotato.squadus.domain.club.match.repository.match.MatchRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchRequestService {

    private final MatchRequestRepository matchRequestRepository;
    private final ClubAdminMemberRepository clubAdminMemberRepository;
    private final ClubRepository clubRepository;
    private final MatchPostRepository matchPostRepository;

    public Page<MatchRequestResponse> getMyClubMatchRequests(Long clubId, Pageable pageable) {
        Page<MatchRequest> requestsPage = matchRequestRepository.findAllByClub_ClubId(clubId, pageable);

        // 유효한 MercenaryPost만 필터링하여 새로운 리스트로 변환
        List<MatchRequestResponse> filteredResponses = requestsPage
                .stream()
                .filter(matchRequest -> isPostValid(matchRequest.getMatchPost()))
                .map(MatchRequestResponse::from)
                .collect(Collectors.toList());

        // 필터링된 리스트를 Page 객체로 변환하여 반환
        return new PageImpl<>(filteredResponses, pageable, requestsPage.getTotalElements());
    }


    public List<MatchRequestResponse> getAllMyClubMatchRequests(Long clubId) {
        return matchRequestRepository.findAllByClub_ClubId(clubId)
                .stream()
                .filter(mercenaryRequest -> isPostValid(mercenaryRequest.getMatchPost()))
                .map(MatchRequestResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelMatchRequest(Long requestId, Long memberId) {
        MatchRequest matchRequest = matchRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 요청을 찾을 수 없습니다."));

        clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(
                        matchRequest.getClub().getClubId(), memberId)
                .orElseThrow(() -> new AppException(ErrorCode.CLUB_ACCESS_DENIED));

        matchRequestRepository.delete(matchRequest);
    }




    public Page<MatchRequestAndMatchPostResponse> getReceivedMatchRequests(Long clubId, Pageable pageable) {

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("동아리를 찾을 수 없습니다."));

        List<MatchPost> matchPosts = matchPostRepository.findByHomeClub(club);

        LocalDateTime now = LocalDateTime.now();

        List<MatchRequestAndMatchPostResponse> allResponses = matchPosts.stream()
                .filter(matchPost -> LocalDateTime.of(matchPost.getMatchStartDate(),matchPost.getMatchStartTime()).isAfter(now))
                .map(matchPost -> {
                    List<ReceivedMatchRequestResponse> receivedRequests = matchPost.getMatchRequests()
                            .stream()
                            .map(ReceivedMatchRequestResponse::from)
                            .collect(Collectors.toList());

                   return MatchRequestAndMatchPostResponse.from(
                           matchPost,
                           receivedRequests
                   );
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

        LocalDateTime now = LocalDateTime.now();

        return matchPosts.stream()
                .filter(matchPost -> LocalDateTime.of(matchPost.getMatchStartDate(),matchPost.getMatchStartTime()).isAfter(now))
                .map(matchPost -> {
                    List<ReceivedMatchRequestResponse> receivedRequests = matchPost.getMatchRequests()
                            .stream()
                            .map(ReceivedMatchRequestResponse::from)
                            .collect(Collectors.toList());

                    return MatchRequestAndMatchPostResponse.from(
                            matchPost,
                            receivedRequests
                    );
                })
                .collect(Collectors.toList());
    }




    @Transactional
    public void decideMatchRequest(Long requestId, String decision, Long memberId) {
        MatchRequest matchRequest = matchRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 요청을 찾을 수 없습니다."));

        clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(
                        matchRequest.getMatchPost().getHomeClub().getClubId(), memberId)
                .orElseThrow(() -> new AppException(ErrorCode.CLUB_ACCESS_DENIED));

        // matchPost가 이미 최종 확정되었는지 확인
        if (matchRequest.getMatchPost().getIsFinalized()) {
            throw new AppException(ErrorCode.CLUB_MATCH_FINALIZE);
        }

        if ("ACCEPTED".equalsIgnoreCase(decision)) {
            matchRequest.accept();
            matchRequest.getMatchPost().finalizeMatch();
        } else if ("REJECTED".equalsIgnoreCase(decision)) {
            matchRequest.reject();
        } else {
            throw new IllegalArgumentException("결정은 ACCEPTED 또는 REJECTED이어야 합니다.");
        }

        matchRequestRepository.save(matchRequest);
    }

    private boolean isPostValid(MatchPost matchPost) {
        LocalDateTime postDateTime = LocalDateTime.of(matchPost.getMatchStartDate(), matchPost.getMatchStartTime());
        return postDateTime.isAfter(LocalDateTime.now());
    }
}

