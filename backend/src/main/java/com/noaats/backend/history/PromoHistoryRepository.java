package com.noaats.backend.history;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoHistoryRepository extends JpaRepository<PromoHistory, Long> {
	List<PromoHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
	Optional<PromoHistory> findByIdAndUserId(Long id, Long userId);
}
