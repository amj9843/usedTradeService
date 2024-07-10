package com.zerobase.used_trade.service.impl;

import com.zerobase.used_trade.component.PageConverter;
import com.zerobase.used_trade.component.SpecificationBuilder;
import com.zerobase.used_trade.data.constant.DomainFilterType;
import com.zerobase.used_trade.data.constant.DomainSortType;
import com.zerobase.used_trade.data.constant.EmployeeSortType;
import com.zerobase.used_trade.data.constant.SubscribeType;
import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.domain.Domain;
import com.zerobase.used_trade.data.dto.DomainDto.DetailInfoResponse;
import com.zerobase.used_trade.data.dto.DomainDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.DomainDto.Principle;
import com.zerobase.used_trade.data.dto.DomainDto.UpdateRequest;
import com.zerobase.used_trade.exception.impl.AlreadyExistsDomainAddressException;
import com.zerobase.used_trade.exception.impl.CannotDeleteDomainBeforeExpiredException;
import com.zerobase.used_trade.exception.impl.NoDomainException;
import com.zerobase.used_trade.repository.DomainRepository;
import com.zerobase.used_trade.repository.UserRepository;
import com.zerobase.used_trade.service.DomainService;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class DomainServiceImpl implements DomainService {
  private final DomainRepository domainRepository;
  private final SpecificationBuilder<Domain> specificationBuilder;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public Principle enrollDomain(EnrollRequest request) {
    //동일한 도메인 주소가 있는지 확인
    if (this.domainRepository.existsByDomainAddress(request.getDomainAddress())) {
      throw new AlreadyExistsDomainAddressException();
    }

    Domain domain = this.domainRepository.save(request.toEntity());

    int headCount = this.userRepository.updateDomainId(domain.getId(),
        "@" + domain.getDomainAddress(), UserRole.ADMIN.name());
    if (headCount > 0) {
      log.info("enroll domainId for people. headcount -> " + headCount);
    }

    return Principle.fromEntity(domain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Principle> getDomainList(String searchCompanyName, String searchDomain, int page, int size,
      DomainSortType criteria, DomainFilterType filter) {

    //검색 조건 쿼리 생성
    Specification<Domain> spec = this.specificationBuilder.init();

    //검색어 따라 검색
    if (searchCompanyName != null && !searchCompanyName.isBlank()) {
      spec = spec.and(this.specificationBuilder.like("companyName", searchCompanyName));
    }
    if (searchDomain != null && !searchDomain.isBlank()) {
      spec = spec.and(this.specificationBuilder.like("domainAddress", searchDomain));
    }
    
    //필터 건 게 있다면 필터에 맞춰 검색
    spec = filter.processing(spec);

    return new PageConverter<Domain>().ListToPage(this.domainRepository.findAll(spec),
            criteria.comparator(), PageRequest.of(page, size)).map(Principle::fromEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public DetailInfoResponse getDomainDetail(Long domainId, int page, int size, EmployeeSortType criteria) {
    Domain domain = this.domainRepository.findById(domainId)
        .orElseThrow(NoDomainException::new);

    /* TODO(UserRepository, CustomUserRepository, ReviewRepository 생성 이후) domainId에 해당하는 유저 목록 조회
    List<Employee> employeeList = this.userRepository.findEmployeeByDomainId(domainId);
    Page<Employee> employeePage = new PageConverter<Employee>().ListToPage(
        employeeList, criteria.comparator(), PageRequest.of(page, size));

    return new DetailInfoResponse(domain, employeePage);
     */
    return new DetailInfoResponse(domain, null);
  }

  @Override
  @Transactional
  public void extendPeriodOfDomain(Long domainId, SubscribeType type) {
    Domain domain = this.domainRepository.findById(domainId)
        .orElseThrow(NoDomainException::new);

    domain.updateEndAt(type.extension(domain.getEndAt()));
  }

  @Override
  @Transactional
  public void updateDomainInfo(Long domainId, UpdateRequest request) {
    Domain domain = this.domainRepository.findById(domainId)
        .orElseThrow(NoDomainException::new);

    if (request.getDomainAddress() != null && !request.getDomainAddress().isBlank()) {
      int headCount = this.userRepository.updateEmailByDomainId(
          domain.getId(), "@" + domain.getDomainAddress(), "@" + request.getDomainAddress());
      log.info("modified domain on email for people. headcount -> " + headCount);
    }

    domain.update(request);
  }

  @Override
  @Transactional
  public void deleteDomain(Long domainId) {
    Domain domain = this.domainRepository.findById(domainId)
        .orElseThrow(NoDomainException::new);

    if (LocalDateTime.now().isBefore(domain.getEndAt())) {
      throw new CannotDeleteDomainBeforeExpiredException();
    }

    int headCount = this.userRepository.updateRoleByDomainId(domainId, UserRole.USER.name());
    if (headCount > 0) {
      log.info("change role by domainId. headcount -> " + headCount);
    }

    this.domainRepository.delete(domain);
  }
}
