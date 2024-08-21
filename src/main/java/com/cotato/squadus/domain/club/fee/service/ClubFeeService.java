package com.cotato.squadus.domain.club.fee.service;

import com.cotato.squadus.api.fee.dto.*;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.common.error.ErrorCode;
import com.cotato.squadus.common.error.exception.AppException;
import com.cotato.squadus.domain.auth.entity.Member;
import com.cotato.squadus.domain.auth.service.ClubMemberService;
import com.cotato.squadus.domain.auth.service.MemberService;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.common.service.ClubService;
import com.cotato.squadus.domain.club.fee.entity.FeePayment;
import com.cotato.squadus.domain.club.fee.entity.FeeType;
import com.cotato.squadus.domain.club.fee.entity.FeeUsage;
import com.cotato.squadus.domain.club.fee.repository.FeePaymentRepository;
import com.cotato.squadus.domain.club.fee.repository.FeeTypeRepository;
import com.cotato.squadus.domain.club.fee.repository.FeeUsageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final FeeUsageRepository feeUsageRepository;
    private final MemberService memberService;


    public ClubFeeSummaryResponseList findAllClubFeeTypesSummary(CustomOAuth2Member customOAuth2Member, Long clubId) {

        List<ClubFeeSummaryResponse> clubFeeSummaryResponseList = feeTypeRepository.findAllByClub_ClubId(clubId)
                .stream().
                map(ClubFeeSummaryResponse::from)
                .toList();

        Long totalBalance = clubFeeSummaryResponseList.stream()
                .mapToLong(ClubFeeSummaryResponse::balance)
                .sum();

        return ClubFeeSummaryResponseList.from(totalBalance, clubFeeSummaryResponseList);
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

//
//    @Transactional
//    public ClubFeeUpdateResponse updateFee(CustomOAuth2Member customOAuth2Member, Long clubId, Long feeTypeId, ClubFeeCreateRequest clubFeeCreateRequest) {
//        FeeType feeType = feeTypeRepository.findById(feeTypeId)
//                .orElseThrow(() -> new EntityNotFoundException("해당 feeTypeId를 가진 동아리 회비를 찾을 수 없습니다."));
//
//        feeType.update(
//                clubFeeCreateRequest.feeTypeName(),
//                clubFeeCreateRequest.price(),
//                clubFeeCreateRequest.memo(),
//                clubFeeCreateRequest.feeCategory(),
//                clubFeeCreateRequest.startDate(),
//                clubFeeCreateRequest.endDate());
//
//        List<FeePayment> feePayments = feeType.getFeePayments();
//        List<Long> clubMemberIds = clubFeeCreateRequest.clubMemberIds();
//
//        for (Long memberId : clubMemberIds) {
//            ClubMember clubMember = clubMemberService.findClubMemberById(memberId);
//
//            FeePayment feePayment = feePaymentRepository.findFeePaymentByClubMemberAndFeeType(clubMember, feeType)
//                    .orElseThrow(() -> new EntityNotFoundException("해당 동아리원이 납부할 회비를 찾을 수 없습니다."));
//
//
//
//            feePaymentRepository.save(feePayment);
//
//            // FeePayment를 리스트에 추가
//            feeType.updateFeePayments(feePayment);
//        }
//
//        feeTypeRepository.save(feeType);
//        return null;
//    }

    @Transactional
    public ClubFeeUsageCreateResponse createClubFeeUsage(CustomOAuth2Member customOAuth2Member, Long clubId, Long feeTypeId, ClubFeeUsageRequest clubFeeUsageRequest) {

        FeeType feeType = feeTypeRepository.findById(feeTypeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 회비를 찾을 수 없습니다."));

        if (clubFeeUsageRequest.price() > feeType.getBalance()) {
            throw new AppException(ErrorCode.NO_BALANCE_ERROR);
        }
        feeType.updateBalance(-clubFeeUsageRequest.price()); // price 만큼 사용

        FeeUsage feeUsage = FeeUsage.builder()
                .feeType(feeType)
                .description(clubFeeUsageRequest.description())
                .price(clubFeeUsageRequest.price())
                .usedAt(clubFeeUsageRequest.usedAt())
                .build();

        FeeUsage savedUsage = feeUsageRepository.save(feeUsage);

        feeType.updateFeeUsages(feeUsage);

        feeTypeRepository.save(feeType);

        return new ClubFeeUsageCreateResponse(savedUsage.getFeeUsageId());
    }

    public ClubFeePaymentInfoResponseList findClubFeePaymentInfo(CustomOAuth2Member customOAuth2Member, Long clubId, Long feeTypeId) {
        Member member = memberService.findMemberByUniqueId(customOAuth2Member.getUniqueId());
        List<FeePayment> feePayments = feePaymentRepository.findAllByFeeType_FeeTypeId(feeTypeId);

        List<ClubFeePaymentInfoResponse> clubFeePaymentInfoResponseList = new ArrayList<>();
        for (FeePayment feePayment : feePayments) {
            ClubFeePaymentInfoResponse clubFeePaymentInfoResponse;
            if (feePayment.getClubMember().getMember().getMemberIdx().equals(member.getMemberIdx())) {
                clubFeePaymentInfoResponse = ClubFeePaymentInfoResponse.from(feePayment, true);
            } else {
                clubFeePaymentInfoResponse = ClubFeePaymentInfoResponse.from(feePayment, false);
            }
            clubFeePaymentInfoResponseList.add(clubFeePaymentInfoResponse);
        }
        return ClubFeePaymentInfoResponseList.from(clubFeePaymentInfoResponseList);

    }

    public ClubFeeUsageResponseList findAllClubFeeUsage(CustomOAuth2Member customOAuth2Member, Long clubId, Long feeTypeId) {

        FeeType feeType = feeTypeRepository.findById(feeTypeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 회비를 찾을 수 없습니다."));
        List<ClubFeeUsageResponse> clubFeeUsageResponseList = feeUsageRepository.findAllByFeeType(feeType)
                .stream()
                .map(ClubFeeUsageResponse::from)
                .toList();

        return ClubFeeUsageResponseList.from(clubFeeUsageResponseList);

    }

    @Transactional
    public ClubFeePaymentUpdateResponse updateClubFeePaymentInfo(CustomOAuth2Member customOAuth2Member, Long clubId, Long feeTypeId, ClubFeePaymentUpdateRequest clubFeePaymentUpdateRequest) {
        List<FeePayment> feePayments = feePaymentRepository.findAllByFeeType_FeeTypeId(feeTypeId);
        FeeType feeType = feeTypeRepository.findById(feeTypeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디를 가진 feeType을 찾을 수 없습니다."));

        Long totalPrice = 0L;
        if(!feeType.getBalance().equals(0L)) {
            throw new AppException(ErrorCode.PAYMENT_CHANGE_DENIED);
        }
        for (FeePayment feePayment : feePayments) {
            Long clubMemberIdx = feePayment.getClubMember().getClubMemberIdx();
            if(clubFeePaymentUpdateRequest.paymentsInfo().get(clubMemberIdx)) {
                feePayment.updateIsPaid(true);
                totalPrice += feeType.getPrice();
            } else {
                feePayment.updateIsPaid(false);
            }
            feePaymentRepository.save(feePayment);
        }
        feeType.updateTotalPrice(totalPrice);
        feeType.updateBalance(totalPrice);
        feeTypeRepository.save(feeType);
        return new ClubFeePaymentUpdateResponse(feeTypeId);
    }

}
