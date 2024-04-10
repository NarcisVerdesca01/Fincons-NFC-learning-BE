package com.fincons.viewaudit;

import java.util.List;

public interface MyAuditCombinatedRepository extends MyViewRepository<AuditCombinated,Long>{

    List<AuditCombinated> findByType(String type);

}
