/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

import com.mycompany.app_fx.FXMLController;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

/**
 *
 * @author adolf
 */
public class Psb extends Task<Void>{
    
    Integer counter;
    Integer size;

    public Psb(Integer counter, Integer size) {
        this.counter = counter;
        this.size = size;
    }
    
    
    @Override
    protected Void call() throws Exception {
 
              updateProgress(counter,size-1);
             
    
        return null;
   
    }

    
}