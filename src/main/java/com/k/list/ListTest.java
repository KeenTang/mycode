package com.k.list;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: keen
 * Date: 2018-08-13
 * Time: 22:47
 */
public class ListTest {
    public static void  testSimpleLinkedList(){
        SimpleLinkedList<Integer> simpleLinkedList=new SimpleLinkedList();
        simpleLinkedList.add(15);
        simpleLinkedList.add(20);
        System.out.println(simpleLinkedList);
        simpleLinkedList.insert(1,30);
        System.out.println(simpleLinkedList);
        simpleLinkedList.insert(2,50);
        System.out.println(simpleLinkedList);
        System.out.println(simpleLinkedList.get(5));
    }
}
