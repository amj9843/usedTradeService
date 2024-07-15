package com.zerobase.used_trade.repository;

import com.zerobase.used_trade.data.domain.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
  List<Account> findAllByUserId(Long userId);

  Long countByUserId(Long userId);

  Optional<Account> findByUserIdAndRepresentativeTrue(Long userId);

  Optional<Account> findFirstByUserIdAndIdNotOrderByIdAsc(Long userId, Long accountId);
}
