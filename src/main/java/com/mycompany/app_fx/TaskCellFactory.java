/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app_fx;

import com.mycompany.helper.ConstructorPost;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author adolf
 */
 public class TaskCellFactory implements Callback<ListView<ConstructorPost>, ListCell<ConstructorPost>> {

    @Override
    public ListCell<ConstructorPost> call(ListView<ConstructorPost> param) {
        return null;
        //return new Cell_listPostController();
    }
}
