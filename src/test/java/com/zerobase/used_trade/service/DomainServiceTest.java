package com.zerobase.used_trade.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.zerobase.used_trade.component.SpecificationBuilder;
import com.zerobase.used_trade.data.constant.DomainFilterType;
import com.zerobase.used_trade.data.constant.DomainSortType;
import com.zerobase.used_trade.data.constant.EmployeeSortType;
import com.zerobase.used_trade.data.constant.SubscribeType;
import com.zerobase.used_trade.data.domain.Domain;
import com.zerobase.used_trade.data.dto.DomainDto.DetailInfoResponse;
import com.zerobase.used_trade.data.dto.DomainDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.DomainDto.Principle;
import com.zerobase.used_trade.data.dto.DomainDto.UpdateRequest;
import com.zerobase.used_trade.repository.DomainRepository;
import com.zerobase.used_trade.repository.UserRepository;
import com.zerobase.used_trade.service.impl.DomainServiceImpl;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class DomainServiceTest {
  @InjectMocks
  private DomainServiceImpl domainService;
  @Mock
  private SpecificationBuilder<Domain> specificationBuilder;
  @Mock
  private DomainRepository domainRepository;
  @Mock
  private UserRepository userRepository;

  public static String domainAddress = "test.com";
  public static String companyName = "test_company";
  public static String businessNumber = "000-00-00000";
  public static String zipCode = "00000";
  public static String roadAddress = "fake_road_address";
  public static String address = "fake_address";
  public static String detail = "";
  public static String phoneNumber = "000-0000-0000";
  public static String period = "ONEYEAR";
  public static String criteria = "NAMEASC";
  public static String filter = "ALL";
  public static int page = 0;
  public static int size = 10;

  @DisplayName("도메인 등록")
  @Test
  void enrollDomain() {
    //given
    EnrollRequest dto = new EnrollRequest(
        domainAddress, companyName, businessNumber, zipCode, roadAddress,
        address, detail, phoneNumber, period
    );

    given(domainRepository.save(any())).willReturn(dto.toEntity());
    given(userRepository.updateDomainId(any(), anyString(), any())).willReturn(2);

    //when
    Principle domain = domainService.enrollDomain(dto);
    ArgumentCaptor<Domain> domainCaptor = ArgumentCaptor.forClass(Domain.class);

    //then
    verify(domainRepository, times(1)).save(domainCaptor.capture());
    verify(userRepository, times(1)).updateDomainId(any(), anyString(), any());

    assertThat(domainCaptor.getValue().getDomainAddress()).isEqualTo(domainAddress);
    assertThat(domainCaptor.getValue().getCompanyName()).isEqualTo(companyName);
    assertThat(domain.getDomainAddress()).isEqualTo(domainAddress);
    assertThat(domain.getCompanyName()).isEqualTo(companyName);
  }

  @DisplayName("등록된 도메인 목록 조회")
  @Test
  void getDomainList() {
    //given
    EnrollRequest dto1 = new EnrollRequest(
        domainAddress, companyName, businessNumber, zipCode, roadAddress,
        address, detail, phoneNumber, period
    );
    EnrollRequest dto2 = new EnrollRequest(
        "test2.com", "company_name2", businessNumber, zipCode, roadAddress,
        address, detail, phoneNumber, period
    );

    given(specificationBuilder.init()).willReturn(null);
    given(domainRepository.findAll((Specification<Domain>) any())).willReturn(Arrays.asList(
        dto1.toEntity(), dto2.toEntity()
    ));

    //when
    Page<Principle> domainPage = domainService.getDomainList(
        null, null, 0, 10,
        DomainSortType.valueOf(criteria), DomainFilterType.valueOf(filter));

    //then
    verify(domainRepository, times(1)).findAll((Specification<Domain>) any());

    assertThat(domainPage.getContent().size()).isEqualTo(2);
    assertThat(domainPage.getContent().get(0).getDomainAddress()).isEqualTo("test2.com");
    assertThat(domainPage.getContent().get(1).getDomainAddress()).isEqualTo(domainAddress);
  }

  @DisplayName("도메인 상세정보 조회")
  @Test
  void getDomainDetail() {
    //given
    Long domainId = 1L;

    EnrollRequest dto = new EnrollRequest(
        domainAddress, companyName, businessNumber, zipCode, roadAddress,
        address, detail, phoneNumber, period
    );

    given(domainRepository.findById(anyLong())).willReturn(Optional.ofNullable(dto.toEntity()));

    //when
    DetailInfoResponse domainDetail = domainService.getDomainDetail(
        1L, 0, 10, EmployeeSortType.valueOf(criteria));

    //then
    verify(domainRepository, times(1)).findById(domainId);

    assertThat(domainDetail.getDomainAddress()).isEqualTo(domainAddress);
    assertThat(domainDetail.getEmployees()).isEqualTo(null);
  }

  @DisplayName("도메인 기간 갱신")
  @Test
  void extendPeriodOfDomain() {
    //given
    Long domainId = 1L;

    EnrollRequest dto = new EnrollRequest(
        domainAddress, companyName, businessNumber, zipCode, roadAddress,
        address, detail, phoneNumber, period
    );

    Domain domain = dto.toEntity();
    LocalDateTime nowEndTime = domain.getEndAt();

    given(domainRepository.findById(anyLong())).willReturn(Optional.of(domain));

    //when
    domainService.extendPeriodOfDomain(domainId, SubscribeType.valueOf(period));

    //then
    verify(domainRepository, times(1)).findById(domainId);

    assertThat(domain.getEndAt()).isEqualTo(nowEndTime.plusMonths(12L));
  }

  @DisplayName("도메인 수정")
  @Test
  void updateDomainInfo() {
    //given
    Long domainId = 1L;

    EnrollRequest enrollDto = new EnrollRequest(
        domainAddress, companyName, businessNumber, zipCode, roadAddress,
        address, detail, phoneNumber, period
    );
    UpdateRequest updateDto = new UpdateRequest(
        "test2.com",
        "change_name",
        null,
        null,
        null,
        null,
        null,
        null
    );

    Domain domain = enrollDto.toEntity();

    given(domainRepository.findById(anyLong())).willReturn(Optional.of(domain));
    given(userRepository.updateEmailByDomainId(any(), anyString(), anyString())).willReturn(2);

    //when
    domainService.updateDomainInfo(domainId, updateDto);

    //then
    verify(domainRepository, times(1)).findById(domainId);
    verify(userRepository, times(1)).updateEmailByDomainId(any(), anyString(), anyString());

    assertThat(domain.getDomainAddress()).isEqualTo("test2.com");
    assertThat(domain.getCompanyName()).isEqualTo("change_name");
    assertThat(domain.getBusinessNumber()).isEqualTo(businessNumber);
  }

  @DisplayName("도메인 삭제")
  @Test
  void deleteDomain() {
    //given
    Long domainId = 1L;

    Domain domain = new Domain(
        domainAddress,
        companyName,
        businessNumber,
        zipCode,
        roadAddress,
        address,
        detail,
        phoneNumber,
        LocalDateTime.now().minusMonths(1L)
    );

    given(domainRepository.findById(anyLong())).willReturn(Optional.of(domain));
    given(userRepository.updateRoleByDomainId(any(), any())).willReturn(2);
    willDoNothing().given(domainRepository).delete((Domain) any());

    //when
    domainService.deleteDomain(domainId);

    //then
    verify(userRepository, times(1)).updateRoleByDomainId(any(), any());
    verify(domainRepository, times(1)).delete((Domain) any());
  }
}
