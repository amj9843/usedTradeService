package com.zerobase.used_trade.component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageConverter<T> {
  public Page<T> ListToPage(List<T> list, Comparator<T> comparator, PageRequest pageRequest) {
    if (!list.isEmpty()) list.sort(comparator);

    int start = (int) pageRequest.getOffset();
    int end = Math.min((start + pageRequest.getPageSize()), list.size());

    return (list.isEmpty()) ? new PageImpl<T>(new ArrayList<>(), pageRequest, 0)
        : new PageImpl<T>(list.subList(start, end), pageRequest, list.size());
  }

  public Page<T> ListToPage(List<T> list, PageRequest pageRequest) {
    int start = (int) pageRequest.getOffset();
    int end = Math.min((start + pageRequest.getPageSize()), list.size());

    return (list.isEmpty()) ? new PageImpl<T>(new ArrayList<>(), pageRequest, 0)
        : new PageImpl<T>(list.subList(start, end), pageRequest, list.size());
  }
}
