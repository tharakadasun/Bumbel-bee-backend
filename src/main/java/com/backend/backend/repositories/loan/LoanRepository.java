package com.backend.backend.repositories.loan;

import com.backend.backend.entities.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan,Integer> {
}
