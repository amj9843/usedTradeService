package com.zerobase.used_trade.repository.custom;

import com.zerobase.used_trade.data.dto.UserDto.Employee;
import java.util.List;

public interface CustomUserRepository {
  List<Employee> findEmployeeByDomainId(Long domainId);
}
