package com.zerobase.used_trade.data.comparator;

import com.zerobase.used_trade.data.dto.UserDto.Employee;
import java.util.Comparator;
import java.util.Objects;

public class EmployeeComparator {

  public static class NameAsc implements Comparator<Employee> {
    @Override
    public int compare(Employee o1, Employee o2) {
      if (!o1.getName().equals(o2.getName())) {
        return o1.getName().compareTo(o2.getName());
      }

      return o1.getId().compareTo(o2.getId());
    }
  }

  public static class ScoreHighest implements Comparator<Employee> {
    @Override
    public int compare(Employee o1, Employee o2) {
      if (o1.getSellScore() < o2.getSellScore()) {
        return 1;
      } else if (o1.getSellScore() == o2.getSellScore()) {
        if (!Objects.equals(o1.getName(), o2.getName())) {
          return o1.getName().compareTo(o2.getName());
        } else {
          return o1.getId().compareTo(o2.getId());
        }
      } return -1;
    }
  }

  public static class ScoreLowest implements Comparator<Employee> {
    @Override
    public int compare(Employee o1, Employee o2) {
      if (o1.getSellScore() > o2.getSellScore()) {
        return 1;
      } else if (o1.getSellScore() == o2.getSellScore()) {
        if (!Objects.equals(o1.getName(), o2.getName())) {
          return o1.getName().compareTo(o2.getName());
        } else {
          return o1.getId().compareTo(o2.getId());
        }
      } return -1;
    }
  }

  public static class CreatedAtDesc implements Comparator<Employee> {
    @Override
    public int compare(Employee o1, Employee o2) {
      return o2.getId().compareTo(o1.getId());
    }
  }

  public static class CreatedAtAsc implements Comparator<Employee> {
    @Override
    public int compare(Employee o1, Employee o2) {
      return o1.getId().compareTo(o2.getId());
    }
  }
}
