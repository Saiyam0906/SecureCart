package com.example.Ecommerce.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.Ecommerce.model.Address;
import com.example.Ecommerce.model.User;

public interface AddressRepository extends JpaRepository<Address, Long>{

	List<Address> findAllByUser(User user);

	Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);

	List<Address> findByUserId(Long userId);

	@Modifying
	@Query("UPDATE Address a SET a.isDefault = :defaultStatus WHERE a.user.id = :userId")
	int updateDefaultStatusByUserId(@Param("userId") Long userId,@Param("defaultStatus") boolean b);

	boolean existsByUserIdAndIsDefaultTrue(Long userId);
	
	

}
