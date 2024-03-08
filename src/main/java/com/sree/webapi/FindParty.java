/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sree.webapi;

import com.azure.spring.cloud.feature.management.FeatureManager;
import com.azure.spring.cloud.feature.management.web.FeatureGate;
import com.sree.webapi.Entity.Employee;
import com.sree.webapi.Entity.EmployeeForm;
import com.sree.webapi.Entity.EmployeeMapper;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author sreep
 */
@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/api/party")
public class FindParty {
    
    //Autowire Feature Manager 
    @Autowired
    FeatureManager featureManager;
    @Autowired
    JdbcTemplate jdbcTemplate;

    //Creating a Party Structure ;
    public record Party(String id, String name, String address){    
        public Party(String id, String name){
            this(id,name,"");
        }
    }
    
    public record Chequing(String account, String id){
        
    }
    public record Savings(String account,String id){
        
    }
    public record Account(Chequing cheq, Savings sav){
        
        public Account(Chequing cheq){
            this(cheq,null);
        }
        
    }
    
      
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('SCOPE_party.read')")
    public ResponseEntity<?> GetParty(@PathVariable String id, @RequestHeader("Authorization") String bearerToken){
        
        System.out.println("in side");
        String name ="Sreejith";
        String address="";
        Party party;
        String[] parts = bearerToken.split("\\.");
        System.out.println(bearerToken);
         
        String payLoad = new String(Base64.getUrlDecoder().decode(parts[1]));
        try{
            address = getAddressDetails();
            // JSONObject.
        JSONObject json = new JSONObject(payLoad);
        System.out.println(json.get("roles"));
            
        }catch(Exception e){
            address= "";
            System.out.println("No authority to execute the function");
        }
        
        party = new Party(id,name,address);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(party);
    }
    @RequestMapping(value="/",method = RequestMethod.GET)
    public ResponseEntity<?> GetParty(){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("Hello World");
    }
    
    @PreAuthorize("hasAuthority('APPROLE_webapiread")
    public String getAddressDetails(){
        return " Canada ,ON";
    }
    
    @RequestMapping(value="/account",method = RequestMethod.GET)    
    @PreAuthorize("hasAuthority('SCOPE_party.read')")
    @FeatureGate(feature = "savings",fallback = "/api/party/accountchq")
    public ResponseEntity<?> GetAccount(){
        
        Optional optional =  Optional.of(featureManager);
        System.out.println(optional.isEmpty()?"null":featureManager.isEnabled("savings").toString());
         
        Chequing cheq = new Chequing("Cheq","1001");
        Savings save = new Savings("Sav","5001");
        Account acc = new Account(cheq,save);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(acc);
    }
    
    @RequestMapping(value="/accountchq",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('SCOPE_party.read')")
    public ResponseEntity<?> GetAccountCheqing(){
        Chequing cheq = new Chequing("Cheq","1001");
        Account acc = new Account(cheq);
        Optional optional =  Optional.of(featureManager);
        if (!optional.isEmpty()){
            //featureManager.
            System.out.println(featureManager.getAllFeatureNames().isEmpty());
            featureManager.getAllFeatureNames().iterator().forEachRemaining((key) ->{
                System.out.println(key);
            });
            if(featureManager.isEnabledAsync("savings").block()){
                System.out.println("Feature is enabled");
            }else{
                System.out.println("Feature is not enabled");
            }
        }
        System.out.println();
        
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(acc);
    }
    
    @PreAuthorize("hasAuthority('SCOPE_party.read')")
    @RequestMapping(method = RequestMethod.GET,value="/employee")
    public ResponseEntity<?> getEmployees(){
        List<Employee> list = jdbcTemplate.query("select id, firstname, lastname, address from Employee", new EmployeeMapper()); 
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list);
    }
    
   @PreAuthorize("hasAuthority('SCOPE_party.read')")
   @PostMapping("/employee")
    public ResponseEntity createEmployee(@RequestBody EmployeeForm emp){
        System.out.println("in side");
        String status ="Failed";
        try{
            jdbcTemplate.update("INSERT INTO EMPLOYEE(firstname,lastname,address) values(?,?,?)", emp.getFirstname(),emp.getLastname(),emp.getAddress());
            status = "Success";
        }catch(Exception e){
            System.out.println(e);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
