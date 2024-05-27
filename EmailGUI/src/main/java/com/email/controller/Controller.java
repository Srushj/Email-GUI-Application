package com.email.controller;

import com.email.entity.EmailGUI;
import com.email.sevice.EmailServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;


@RestController
@CrossOrigin
public class Controller
{
    @Autowired
    private EmailServiceImp service;

    @RequestMapping(value = "/sendmail", method = RequestMethod.POST)
    public String save(@RequestParam String recipient,
    				   @RequestParam String subject, 
    				   @RequestParam String msg, 
    				   @RequestParam MultipartFile file) throws MessagingException {
    	
    	try {
            boolean result = service.sendEmail(recipient, subject, msg, file);
            if (result) {
                return "Email sent successfully...";
            } else {
                return "Email not sent !!!";
            }
        } catch (MessagingException e) {
            e.printStackTrace(); // Log or handle the exception properly
            return "Error sending email: " + e.getMessage();
        }
        }
    
    	@GetMapping("/login")
    	public String getRequest() {
    		return "Welcome";
    	}
    }

