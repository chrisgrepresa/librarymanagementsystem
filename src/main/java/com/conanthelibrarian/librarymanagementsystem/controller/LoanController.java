package com.conanthelibrarian.librarymanagementsystem.controller;

import com.conanthelibrarian.librarymanagementsystem.dto.LoanDTO;
import com.conanthelibrarian.librarymanagementsystem.dto.UserDTO;
import com.conanthelibrarian.librarymanagementsystem.service.LoanService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/loan")
@Log4j2
public class LoanController {

    private final LoanService loanService;

    //todo LOGs y ResponeEntity
    @GetMapping("/all")
    public List<LoanDTO> findAllLoan(){
        return loanService.findLoan();
    }

    @PostMapping("/new")
    public void newLoan(@RequestBody LoanDTO loanDTO){
        loanService.newLoan(loanDTO);
    }


}
