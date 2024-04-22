package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.domain.User;

public interface UserCheck {
    User findUserByEmail(String email);
}
