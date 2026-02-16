package com.abcm.kyc.service.ui.config;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.abcm.kyc.service.ui.repository.AppUserRepository;
import com.abcm.kyc.service.ui.repository.MerchantRepository;
import com.abcmkyc.entity.AppUser;
import com.abcmkyc.entity.Merchant_Master;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final AppUserRepository appUserRepo;
    private final MerchantRepository merchantRepo;

    public CustomUserDetailsService(AppUserRepository appUserRepo, MerchantRepository merchantRepo) {
        this.appUserRepo = appUserRepo;
        this.merchantRepo = merchantRepo;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Object userObj = appUserRepo.findByCredentialsUsername(username)
            .<Object>map(user -> (Object) user)
            .orElseGet(() -> merchantRepo.findByCredentialsUsername(username).orElse(null));

        if (userObj instanceof AppUser user) {
            return buildSpringUser(user.getCredentials().getUsername(), user.getCredentials().getPassword(), user.getRole().getRoleName());
        }

        if (userObj instanceof Merchant_Master merchant) {
            return buildSpringUser(merchant.getCredentials(). getUsername(), merchant.getCredentials().getPassword(), merchant.getRole().getRoleName());
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }

    private UserDetails buildSpringUser(String username, String password, String roleName) {
        return new org.springframework.security.core.userdetails.User(
                username,
                password,
                Collections.singleton(new SimpleGrantedAuthority(roleName))
        );
    }

}

