package com.inn.cafe.management.dao;

import com.inn.cafe.management.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillDao extends JpaRepository<Bill,Integer> {

}
