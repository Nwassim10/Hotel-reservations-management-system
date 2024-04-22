package edu.miu.cs.cs544.domain.dto;

import edu.miu.cs.cs544.domain.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;

    private String userName;

    private String firstName;

    private String lastName;

    private String email;

    private String userPass;

    private Boolean active;

    private AuditDataDTO auditDataDTO;

    private RoleType roleType;

    private String matchingPassword;

    public UserDTO(Integer id, String userName, String userPass, Boolean active, AuditDataDTO auditDataDTO, RoleType roleType) {
    this.id = id;
    this.userName = userName;
    this.userPass = userPass;
    this.active = active;
    this.auditDataDTO = auditDataDTO;
    this.roleType = roleType;
    }
}
