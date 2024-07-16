package com.zerobase.used_trade.repository;

import com.zerobase.used_trade.data.domain.Address;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
  Long countByUserId(Long userId);

  List<Address> findAllByUserId(Long userId);

  Optional<Address> findByUserIdAndRepresentativeTrue(Long userId);

  Optional<Address> findFirstByUserIdAndIdNotOrderByIdAsc(Long userId, Long addressId);
}
