package com.midai.pay.customer.service;

import java.util.List;

import com.midai.pay.customer.entity.BoCustomerInst;

public interface BoCustomerInstService {
    int deleteCustomerInst(String mercNo);
    
    int insertCustomerInst(String mercNo,String instCode);
    
    List<BoCustomerInst> selectCustomerInst(String mercNo);
}
