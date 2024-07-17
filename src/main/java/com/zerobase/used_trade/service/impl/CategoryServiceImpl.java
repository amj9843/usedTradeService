package com.zerobase.used_trade.service.impl;

import com.zerobase.used_trade.component.PageConverter;
import com.zerobase.used_trade.data.constant.CategorySortType;
import com.zerobase.used_trade.data.constant.UserRole;
import com.zerobase.used_trade.data.domain.Category;
import com.zerobase.used_trade.data.domain.Product;
import com.zerobase.used_trade.data.domain.ProductMatchCategory;
import com.zerobase.used_trade.data.domain.User;
import com.zerobase.used_trade.data.dto.CategoryDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.CategoryDto.Principle;
import com.zerobase.used_trade.data.dto.CategoryDto.UpdateRequest;
import com.zerobase.used_trade.data.dto.ProductMatchCategoryDto;
import com.zerobase.used_trade.exception.impl.AlreadyExistsCategoryException;
import com.zerobase.used_trade.exception.impl.CannotDeleteContainProductException;
import com.zerobase.used_trade.exception.impl.NoAuthorizeException;
import com.zerobase.used_trade.exception.impl.NoCategoryException;
import com.zerobase.used_trade.exception.impl.NoUserException;
import com.zerobase.used_trade.repository.CategoryRepository;
import com.zerobase.used_trade.repository.ProductMatchCategoryRepository;
import com.zerobase.used_trade.repository.ProductRepository;
import com.zerobase.used_trade.repository.UserRepository;
import com.zerobase.used_trade.service.CategoryService;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;
  private final ProductMatchCategoryRepository matchRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public Principle enrollCategory(Long userId, EnrollRequest request) {
    //TODO TOKEN 생성 이후엔 CustomUserDetails 로 받을 예정이므로 굳이 불러올 필요 없음
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);
    if (user.getRole() != UserRole.ADMIN) {
      throw new NoAuthorizeException();
    }

    try {
      Category category = this.categoryRepository.save(request.toEntity());

      return Principle.fromEntity(category);
    } catch (DataIntegrityViolationException e) {
      //등록하려는 카테고리명이 중복되는 경우
      throw new AlreadyExistsCategoryException();
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Principle> getCategoryList(int page, int size, CategorySortType criteria) {
    return new PageConverter<Category>().ListToPage(this.categoryRepository.findAll(),
            criteria.comparator(), PageRequest.of(page, size)).map(Principle::fromEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public Principle getCategory(Long categoryId) {
    return Principle.fromEntity(this.categoryRepository.findById(categoryId)
        .orElseThrow(NoCategoryException::new));
  }

  @Override
  @Transactional
  public void updateCategoryInfo(Long userId, Long categoryId, UpdateRequest request) {
    //TODO TOKEN 생성 이후엔 CustomUserDetails 로 받을 예정이므로 굳이 불러올 필요 없음
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);
    if (user.getRole() != UserRole.ADMIN) {
      throw new NoAuthorizeException();
    }

    Category category = this.categoryRepository.findById(categoryId)
        .orElseThrow(NoCategoryException::new);

    try {
      category.update(request);
    } catch (DataIntegrityViolationException e) {
      //등록하려는 카테고리명이 중복되는 경우
      throw new AlreadyExistsCategoryException();
    }
  }

  @Override
  @Transactional
  public void deleteCategory(Long userId, Long categoryId) {
    //TODO TOKEN 생성 이후엔 CustomUserDetails 로 받을 예정이므로 굳이 불러올 필요 없음
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);
    if (user.getRole() != UserRole.ADMIN) {
      throw new NoAuthorizeException();
    }

    Category category = this.categoryRepository.findById(categoryId)
        .orElseThrow(NoCategoryException::new);

    if (this.matchRepository.existsByCategoryId(categoryId)) {
      //카테고리에 포함되는 상품이 있는 경우 삭제 불가
      throw new CannotDeleteContainProductException();
    }

    this.categoryRepository.delete(category);
  }

  @Override
  @Transactional
  public void enrollProductsOnCategory(Long userId, Long categoryId, Set<Long> productIdList) {
    //TODO TOKEN 생성 이후엔 CustomUserDetails 로 받을 예정이므로 굳이 불러올 필요 없음
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);
    if (user.getRole() != UserRole.ADMIN) {
      throw new NoAuthorizeException();
    }

    if (this.categoryRepository.findById(categoryId).isEmpty()) {
      throw new NoCategoryException();
    }

    productIdList.forEach(productId-> {
      Optional<Product> product = this.productRepository.findById(productId);
      if (product.isEmpty()) {
        log.info(
            "cannot match product on category cause there's no product by insert id-> productId: {}, categoryId: {} ",
            productId, categoryId);
      } else {
        try {
          //매치테이블에 등록
          this.matchRepository.save(ProductMatchCategoryDto.EnrollRequest.toEntity(productId, categoryId));
        } catch(DataIntegrityViolationException e) {
          //이미 등록된 항목일 때
          log.info("already enroll product on category-> productId: {}, categoryId: {} ", productId, categoryId);
        } catch(Exception e) {
          log.info(
              "cannot match product on category-> productId: {}, categoryId: {} ",
              productId, categoryId);
        }
      }
    });
  }

  @Override
  @Transactional
  public void deleteProductsOnCategory(Long userId, Long categoryId, Set<Long> productIdList) {
    //TODO TOKEN 생성 이후엔 CustomUserDetails 로 받을 예정이므로 굳이 불러올 필요 없음
    User user = this.userRepository.findById(userId).orElseThrow(NoUserException::new);
    if (user.getRole() != UserRole.ADMIN) {
      throw new NoAuthorizeException();
    }

    if (this.categoryRepository.findById(categoryId).isEmpty()) {
      throw new NoCategoryException();
    }

    productIdList.forEach(productId-> {
      Optional<ProductMatchCategory> match =
          this.matchRepository.findByProductIdAndCategoryId(productId, categoryId);
      if (match.isEmpty()) {
        log.info(
            "cannot delete product on category cause there's no matching information-> productId: {}, categoryId: {} ",
            productId, categoryId);
      } else {
        try {
          //매치테이블에서 삭제
          this.matchRepository.delete(match.get());
        } catch(Exception e) {
          log.info(
              "cannot delete product on category-> productId: {}, categoryId: {} ",
              productId, categoryId);
        }
      }
    });
  }
}
