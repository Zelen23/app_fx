/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

/**
 *
 * @author adolf
 */
public class ConstructorProvider {
    
  public String name;
  public  String plase;
  public  Integer id;
  public   Boolean flag;
  public String type;

    public ConstructorProvider(String name, String plase, Integer id, Boolean flag,String type) {
        this.name = name;
        this.plase = plase;
        this.id = id;
        this.flag = flag;
        this.type=type;
    }
    
        
}
