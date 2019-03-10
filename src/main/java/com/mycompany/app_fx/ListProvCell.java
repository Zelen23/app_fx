/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app_fx;

import com.mycompany.helper.ConstructorProvider;
import javafx.scene.control.ListCell;

/**
 *
 * @author adolf
 */
public class ListProvCell extends ListCell<ConstructorProvider> {

    @Override
    protected void updateItem(ConstructorProvider item, boolean empty) {
        super.updateItem(item, empty); 
    if(item != null)
        {
            Cell_listAddProviderController cell_listAddProvider=new Cell_listAddProviderController();
            cell_listAddProvider.setInfo(item);
            setGraphic(cell_listAddProvider.getBox());
            
           
        }//To change body of generated methods, choose Tools | Templates.//To change body of generated methods, choose Tools | Templates.
    }
    
    
 

}
