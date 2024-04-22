package edu.miu.cs.cs544.domain.adapter;

import edu.miu.cs.cs544.domain.AuditData;
import edu.miu.cs.cs544.domain.dto.AuditDataDTO;

public class AuditDataAdapter {
    public static AuditData getAuditData(AuditDataDTO auditDataDTO) {
        if (auditDataDTO == null) {
            return new AuditData();
        }
        return new AuditData(
                auditDataDTO.getCreatedBy(),
                auditDataDTO.getUpdatedBy(),
                auditDataDTO.getCreatedOn(),
                auditDataDTO.getUpdatedOn()
        );
    }

    public static AuditDataDTO getAuditDataDTO(AuditData auditData) {
        if (auditData == null) {
            return new AuditDataDTO();
        }
        return new AuditDataDTO(
                auditData.getCreatedBy(),
                auditData.getUpdatedBy(),
                auditData.getCreatedOn(),
                auditData.getUpdatedOn()
        );
    }
}
