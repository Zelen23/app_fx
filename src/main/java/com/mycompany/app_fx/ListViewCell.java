/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app_fx;

import com.mycompany.helper.ConstructorPost;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;

/**
 *
 * @author adolf
 */
public class ListViewCell  extends ListCell<ConstructorPost>{ 

 
    
    @Override
    protected void updateItem(ConstructorPost item, boolean empty) {
        super.updateItem(item, empty); 
            if(item != null)
        {
            Cell_listPostController cell_listPostController=new Cell_listPostController();
            cell_listPostController.setInfo(item);
            setGraphic(cell_listPostController.getBox());
            
           
        }//To change body of generated methods, choose Tools | Templates.
            
            
    }
    
    
    
    
    
}
