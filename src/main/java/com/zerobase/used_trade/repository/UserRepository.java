package com.zerobase.used_trade.repository;

import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.repository.custom.CustomUserRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
  Optional<User> findByEmail(String email);

  @Modifying
  @Query(value = "update User u set u.domain_id = :domainId, u.role = :role where u.email like %:domainAddress",
      nativeQuery = true)
  int updateDomainId(
      @Param("domainId") Long domainId, @Param("domainAddress") String domainAddress, @Param("role") String role);

  @Modifying
  @Query(value =
      "update User u set u.email = replace(u.email, :originDomainAddress, :changeDomainAddress) "
          + "where u.domain_id = :domainId", nativeQuery = true)
  int updateEmailByDomainId(
      @Param("domainId") Long domainId, @Param("originDomainAddress") String originDomainAddress,
      @Param("changeDomainAddress") String changeDomainAddress);

  @Modifying
  @Query(value = "update User u set u.role = :role where u.domain_id = :domainId", nativeQuery = true)
  int updateRoleByDomainId(@Param("domainId") Long domainId, @Param("role") String role);
}
