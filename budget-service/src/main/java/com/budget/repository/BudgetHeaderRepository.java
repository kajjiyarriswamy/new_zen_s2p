package com.budget.repository;

import com.budget.entity.BudgetHeaderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BudgetHeaderRepository extends JpaRepository<BudgetHeaderEntity, Long> {
    Optional<BudgetHeaderEntity> findByBudgetId(String budgetId);
}
