package com.example.complaint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan // registers the @WebServlet classes
public class ComplaintApplication {
    public static void main(String[] args) {
        SpringApplication.run(ComplaintApplication.class, args);
    }
}
