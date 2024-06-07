package com.chen.srb.core.service;

import com.chen.srb.core.pojo.dto.BorrowerDTO;

public interface BorrowerService {

    

    void saveBorrower(BorrowerDTO borrowerDTO, Long userId);

    Integer getBorrowerStatus(Long userId);
}
