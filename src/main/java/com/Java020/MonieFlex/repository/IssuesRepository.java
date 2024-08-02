package com.Java020.MonieFlex.repository;

import com.Java020.MonieFlex.domain.entities.Issues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuesRepository extends JpaRepository<Issues, Long> {
}
