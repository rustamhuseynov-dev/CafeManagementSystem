package com.inn.cafe.management.dao;

import com.inn.cafe.management.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillDao extends JpaRepository<Bill,Integer> {

    List<Bill> getAllList();

    List<Bill> getBillByUsername(@Param("username") String currentUser);
}
