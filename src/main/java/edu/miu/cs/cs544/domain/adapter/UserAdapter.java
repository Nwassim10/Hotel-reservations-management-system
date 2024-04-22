package edu.miu.cs.cs544.domain.adapter;

import edu.miu.cs.cs544.domain.User;
import edu.miu.cs.cs544.domain.dto.UserDTO;

public class UserAdapter {
    public static UserDTO getUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUserName(),
                user.getUserPass(),
                user.getActive(),
                AuditDataAdapter.getAuditDataDTO(user.getAuditData()),
                user.getRoleType()
        );
    }

    public static User getUser(UserDTO userDTO) {
        return new User(
                userDTO.getId(),
                userDTO.getUserName(),
                userDTO.getUserPass(),
                userDTO.getActive(),
                AuditDataAdapter.getAuditData(userDTO.getAuditDataDTO()),
                userDTO.getRoleType()
        );
    }
}
