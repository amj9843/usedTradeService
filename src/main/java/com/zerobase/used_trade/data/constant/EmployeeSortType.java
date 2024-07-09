package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

import com.zerobase.used_trade.data.comparator.EmployeeComparator.CreatedAtAsc;
import com.zerobase.used_trade.data.comparator.EmployeeComparator.CreatedAtDesc;
import com.zerobase.used_trade.data.comparator.EmployeeComparator.NameAsc;
import com.zerobase.used_trade.data.comparator.EmployeeComparator.ScoreHighest;
import com.zerobase.used_trade.data.comparator.EmployeeComparator.ScoreLowest;
import com.zerobase.used_trade.data.constant.aware.DescriptionAware;
import com.zerobase.used_trade.data.constant.aware.SortTypeAware;
import com.zerobase.used_trade.data.dto.UserDto.Employee;
import java.util.Comparator;

public enum EmployeeSortType implements DescriptionAware, SortTypeAware<Employee> {
  NAMEASC("이름순", new NameAsc()),

  SCOREHIGHEST("판매점수 높은 순", new ScoreHighest()),

  SCORELOWEST("판매점수 낮은 순", new ScoreLowest()),
  CREATEDATDESC("최근 가입순", new CreatedAtDesc()),
  CREATEDATASC("가입순", new CreatedAtAsc());

  private final String description;
  private final Comparator<Employee> comparator;

  EmployeeSortType(String description, Comparator<Employee> comparator) {
    this.description = description;
    this.comparator = comparator;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public Comparator<Employee> comparator() {
    return this.comparator;
  }

  @Override
  public String toString() {
    return format("%s(%s)", this.name(), this.description);
  }
}
