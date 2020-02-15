package com.code.to.learn.web.web_service;

import com.code.to.learn.persistence.dao.api.UserDao;
import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.hibernate.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserDao userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Optional<User> userByUsername = userRepository.findByUsername(username, session);
            return userByUsername.orElseThrow(() -> new UsernameNotFoundException(username));
        }
    }
}
