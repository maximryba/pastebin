package com.example.pasteservice.security;


import com.example.pasteservice.entity.User;
import com.example.pasteservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSecurityService {

    private final UserRepository userRepository;

    public boolean isThisUserOrAdmin(Long userId, Authentication authentication) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return false;
        }

        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        return user.getUsername().equals(username) || isAdmin;
    }

}
