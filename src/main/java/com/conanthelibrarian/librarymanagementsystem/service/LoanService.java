package com.conanthelibrarian.librarymanagementsystem.service;

import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import com.conanthelibrarian.librarymanagementsystem.mapper.LoanMapper;
import com.conanthelibrarian.librarymanagementsystem.repository.LoanRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;

    public List<LoanDTO> findLoan(){
        return loanRepository.findAll().stream()
                .map(loanMapper::loanToLoanDTO)
                .collect(Collectors.toList());
    }

    //todo LOGS
}
