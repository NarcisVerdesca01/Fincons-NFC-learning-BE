package com.fincons.viewaudit;

import java.util.List;

public interface IAuditCombinatedService {

    List<AuditCombinated> findAllAuditCombinated();

    Long[] findAllAuditCombinatedCourseInformation();

    Long[] findAllAuditCombinatedQuizInformation();
}
