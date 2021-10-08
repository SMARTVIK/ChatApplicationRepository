package com.vik.learningchatapplication;

import java.util.ArrayList;

public class StackExample {

    private ArrayList<Integer> list; //to store the elements

    StackExample() {
        list = new ArrayList<>(); //1,2,3     pop 3, remaining elements -> 1, 2
    }

    public void push(int num) {
        list.add(num);
    }

    public Integer pop() {
        //we have to remove the last element
        if(!isEmpty()) {
            int num = list.get(list.size()-1);  //1, 2, 3  pop 1,2
            list.remove(num);
            return num;
        }
        return null;
    }

    public Integer peek() {
        if(!isEmpty()) {
            return list.get(list.size()-1);
        }
        return null;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

}
