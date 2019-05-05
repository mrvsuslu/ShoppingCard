package com.system.shoppingcard.repository;

import com.system.shoppingcard.domain.ShoppingCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCardRepository extends JpaRepository<ShoppingCard, Long> {
}
