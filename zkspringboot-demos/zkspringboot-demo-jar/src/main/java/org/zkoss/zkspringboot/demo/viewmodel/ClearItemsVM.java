package org.zkoss.zkspringboot.demo.viewmodel;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.ListModelList;

import java.util.*;

public class ClearItemsVM {

    private ListModelList<String> model = new ListModelList<>();
    private List dataList = new LinkedList();
    public ClearItemsVM() {
        for (int i = 0; i < 20000; i++) {
            dataList.add("item"+ i);
        }
    }

    @Command
    public void fill() {
        model.addAll(dataList);
    }
    @Command
    public void clear() {
        model.clear();
    }

    public ListModelList<String> getModel() {
        return model;
    }

    public void setModel(ListModelList<String> model) {
        this.model = model;
    }

}
