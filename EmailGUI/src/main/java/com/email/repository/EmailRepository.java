package com.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.email.entity.EmailGUI;

public interface EmailRepository extends JpaRepository<EmailGUI, Integer>{

}
