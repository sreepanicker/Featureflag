/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sree.webapi.Entity;


import jakarta.persistence.Entity;
import org.springframework.data.annotation.Id;

/**
 *
 * @author sreep
 *
**/


public class Employee {
    
    
    public Employee(){        
    }
    
     public Employee(long id, String firstname, String lastname, String address){     
         this.id = id;
         this.firstname= firstname;
         this.lastname = lastname;
         this.address = address;
    }
    @Id    
    private Long id;
    
    private String firstname;
    private String lastname;
    private String address;

   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    
            
}
