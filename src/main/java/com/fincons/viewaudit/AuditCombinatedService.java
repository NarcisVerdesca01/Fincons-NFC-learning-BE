package com.fincons.viewaudit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditCombinatedService implements IAuditCombinatedService{

    @Autowired
    private MyAuditCombinatedRepository myAuditCombinatedRepository;

    @Override
    public List<AuditCombinated> findAllAuditCombinated() {
        return myAuditCombinatedRepository.findAll();
    }



}
