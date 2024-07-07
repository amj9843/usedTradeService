package com.zerobase.used_trade.controller;

import static java.lang.String.format;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.used_trade.component.PageConverter;
import com.zerobase.used_trade.data.constant.DomainSortType;
import com.zerobase.used_trade.data.constant.SuccessCode;
import com.zerobase.used_trade.data.domain.Domain;
import com.zerobase.used_trade.data.dto.DomainDto.DetailInfoResponse;
import com.zerobase.used_trade.data.dto.DomainDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.DomainDto.ExtensionRequest;
import com.zerobase.used_trade.data.dto.DomainDto.Principle;
import com.zerobase.used_trade.data.dto.DomainDto.UpdateRequest;
import com.zerobase.used_trade.service.DomainService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DomainController.class)
public class DomainControllerTest {
  @Autowired
  private MockMvc mvc;
  @MockBean
  private DomainService domainService;
  @Autowired
  private ObjectMapper objectMapper;

  public static String URL = "/domain";
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

  @DisplayName("관리자 도메인 등록")
  @WithMockUser
  @Test
  void enrollDomain() throws Exception {
    //given
    EnrollRequest dto = new EnrollRequest(
        domainAddress, companyName, businessNumber, zipCode, roadAddress,
        address, detail, phoneNumber, period
    );

    //when
    when(domainService.enrollDomain(any())).thenReturn(Principle.fromEntity(dto.toEntity()));

    //then
    mvc.perform(
            post(format("%s/enroll", URL))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(SuccessCode.CREATED_SUCCESS.status().value()))
        .andExpect(jsonPath("$.data.domainAddress").value(domainAddress));
  }

  @DisplayName("등록된 관리자 도메인 목록 조회")
  @WithMockUser
  @Test
  void getDomainList() throws Exception {
    //given
    EnrollRequest dto1 = new EnrollRequest(
        domainAddress, companyName, businessNumber, zipCode, roadAddress,
        address, detail, phoneNumber, period
    );
    EnrollRequest dto2 = new EnrollRequest(
        "test2.com", "company_name2", businessNumber, zipCode, roadAddress,
        address, detail, phoneNumber, period
    );

    List<Domain> domainList = Arrays.asList(dto1.toEntity(), dto2.toEntity());

    //when
    when(domainService.getDomainList(any(), any(), anyInt(), anyInt(), any(), any()))
        .thenReturn(
            new PageConverter<Domain>().ListToPage(
                domainList, DomainSortType.valueOf(criteria).comparator(), PageRequest.of(page, size)
            ).map(Principle::fromEntity)
        );

    //then
    mvc.perform(
            get(format("%s", URL))
                .contentType(MediaType.APPLICATION_JSON)
                .param("companyName", (String) null)
                .param("domain", (String) null)
                .param("page", (String) null)
                .param("size", (String) null)
                .param("criteria", (String) null)
                .param("filter", (String) null)
                .with(csrf())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(SuccessCode.READ_SUCCESS.status().value()))
        .andExpect(jsonPath("$.data.content", hasSize(2)));
  }

  @DisplayName("관리자 도메인 상세정보 확인")
  @WithMockUser
  @Test
  void getDomainDetail() throws Exception {
    //given
    Long id = 1L;

    EnrollRequest dto = new EnrollRequest(
        domainAddress, companyName, businessNumber, zipCode, roadAddress,
        address, detail, phoneNumber, period
    );

    Domain domain = dto.toEntity();

    //when
    when(domainService.getDomainDetail(anyLong(), anyInt(), anyInt(), any()))
        .thenReturn(new DetailInfoResponse(domain, null));

    //then
    mvc.perform(
        get(format("%s/detail/{id}", URL), id)
            .contentType(MediaType.APPLICATION_JSON)
            .param("page", (String) null)
            .param("size", (String) null)
            .param("criteria", (String) null)
            .with(csrf())
    )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(SuccessCode.READ_SUCCESS.status().value()))
        .andExpect(jsonPath("$.data.domainAddress").value(domainAddress))
        .andExpect(jsonPath("$.data.valid").value(true))
        .andExpect(jsonPath("$.data.companyInformation.name").value(companyName))
        .andExpect(jsonPath("$.data.employees").doesNotExist());
  }

  @DisplayName("관리자 도메인 갱신")
  @WithMockUser
  @Test
  void extendPeriod() throws Exception {
    //given
    Long id = 1L;
    ExtensionRequest dto = new ExtensionRequest(period);

    //when
    doNothing().when(domainService).extendPeriodOfDomain(anyLong(), any());

    //then
    mvc.perform(
            patch(format("%s/extension/{id}", URL), id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(SuccessCode.EXTEND_PERIOD_SUCCESS.status().value()))
        .andExpect(jsonPath("$.data").doesNotExist());
  }

  @DisplayName("도메인 정보 수정")
  @WithMockUser
  @Test
  void updateDomain() throws Exception {
    //given
    Long id = 1L;

    UpdateRequest dto = new UpdateRequest(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    );

    //when
    doNothing().when(domainService).updateDomainInfo(anyLong(), any());

    //then
    mvc.perform(
            patch(format("%s/update/{id}", URL), id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(SuccessCode.UPDATED_SUCCESS.status().value()))
        .andExpect(jsonPath("$.data").doesNotExist());
  }

  @DisplayName("도메인 정보 삭제")
  @WithMockUser
  @Test
  void deleteDomain() throws Exception {
    //given
    Long id = 1L;

    //when
    doNothing().when(domainService).deleteDomain(id);

    //then
    mvc.perform(
        delete(format("%s/delete/{id}", URL), id)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(SuccessCode.DELETED_SUCCESS.status().value()))
        .andExpect(jsonPath("$.data").doesNotExist());
  }
}
