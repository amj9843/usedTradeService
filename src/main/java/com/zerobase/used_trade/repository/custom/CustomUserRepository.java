package com.zerobase.used_trade.repository.custom;

import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.UserDto.Employee;
import java.util.List;
import java.util.Optional;

public interface CustomUserRepository {
  List<Employee> findEmployeeByDomainId(Long domainId);
  Optional<User> findDealerByDealId(Long dealId);
}
