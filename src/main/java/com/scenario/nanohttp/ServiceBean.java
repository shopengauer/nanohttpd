package com.scenario.nanohttp;

import org.springframework.stereotype.Service;

/**
 * Created by vasiliy on 11.03.17.
 */
@Service
public class ServiceBean {


    public String sum(String a,String b){
        return  a+b ;
    }

}
