package com.cotato.squadus.domain.club.fee.service;

import com.cotato.squadus.api.fee.dto.ClubFeeCreateRequest;
import com.cotato.squadus.api.fee.dto.ClubFeeCreateResponse;
import com.cotato.squadus.api.fee.dto.ClubFeeSummaryResponse;
import com.cotato.squadus.api.fee.dto.ClubFeeSummaryResponseList;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.domain.auth.service.ClubMemberService;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.common.service.ClubService;
import com.cotato.squadus.domain.club.fee.entity.FeePayment;
import com.cotato.squadus.domain.club.fee.entity.FeeType;
import com.cotato.squadus.domain.club.fee.repository.FeePaymentRepository;
import com.cotato.squadus.domain.club.fee.repository.FeeTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubFeeService {

    private final FeeTypeRepository feeTypeRepository;
    private final FeePaymentRepository feePaymentRepository;
    private final ClubMemberService clubMemberService;
    private final ClubService clubService;


    public ClubFeeSummaryResponseList findAllClubFeeTypesSummary(CustomOAuth2Member customOAuth2Member, Long clubId) {

        List<ClubFeeSummaryResponse> clubFeeSummaryResponseList = feeTypeRepository.findAllByClub_ClubId(clubId).stream().map(ClubFeeSummaryResponse::from).toList();
        return ClubFeeSummaryResponseList.from(clubFeeSummaryResponseList);
    }

    @Transactional
    public ClubFeeCreateResponse createFee(CustomOAuth2Member customOAuth2Member, Long clubId, ClubFeeCreateRequest clubFeeCreateRequest) {

        Club club = clubService.findClubByClubId(clubId);

        FeeType feeType = FeeType.builder()
                .club(club)
                .feeTypeName(clubFeeCreateRequest.feeTypeName())
                .price(clubFeeCreateRequest.price())
                .memo(clubFeeCreateRequest.memo())
                .feeCategory(clubFeeCreateRequest.feeCategory())
                .startDate(clubFeeCreateRequest.startDate())
                .endDate(clubFeeCreateRequest.endDate())
                .balance(0L)
                .totalPrice(0L)
                .build();

        List<Long> clubMemberIds = clubFeeCreateRequest.clubMemberIds();

        for (Long memberId : clubMemberIds) {
            ClubMember clubMember = clubMemberService.findClubMemberById(memberId);

            // FeePayment 생성 및 초기화
            FeePayment feePayment = FeePayment.builder()
                    .isPaid(false) // 초기화 시 납부 여부를 false로 설정
                    .feeType(feeType)
                    .clubMember(clubMember)
                    .build();

            feePaymentRepository.save(feePayment);

            // FeePayment를 리스트에 추가
            feeType.updateFeePayments(feePayment);
        }
        feeTypeRepository.save(feeType);

        return new ClubFeeCreateResponse(feeType.getFeeTypeId());
    }


}
