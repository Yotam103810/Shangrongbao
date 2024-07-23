package com.chen.srb.core.service;

import com.chen.srb.core.pojo.dto.BorrowerDTO;
import com.chen.srb.core.pojo.vo.BorrowerDetailVO;
import com.chen.srb.core.pojo.vo.BorrowerVO;

public interface BorrowerService {

    

    void saveBorrower(BorrowerDTO borrowerDTO, Long userId);

    Integer getBorrowerStatus(Long userId);

    BorrowerVO listPage(Integer page, Integer limit, String keyword);

    BorrowerDetailVO getBorrowerDetailVOById(Long id);
}
