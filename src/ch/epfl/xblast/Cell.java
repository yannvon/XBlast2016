//-------Project:XBlast
//-------Name:LoicVandenberghe

//------------------------------------
package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Cell {

    public final static int COLUMNS=15,ROWS=13,COUNT=ROWS*COLUMNS;
    public final static List<Cell> ROW_MAJOR_ORDER=Collections.unmodifiableList(rowMajorOrder());
    public final static List<Cell> SPIRAL_ORDER;
    private final int x,y;
    
    //retourne le tableau de cellules dans l'ordre de lecture
    private static ArrayList<Cell> rowMajorOrder(){
        ArrayList<Cell> order= new ArrayList<Cell>();
        
        for(int i=0;i<COLUMNS;i++){
            for(int j=0;j<ROWS;j++){
                order.add(new Cell(i,j));
            }
        }
        return order;
    }
    
    //retourne le tableau de cellules dans l'ordre spirale
    private static ArrayList<Cell> spiralOrder(){
        //test
        ArrayList<Integer> ix=new ArrayList<Integer>();
        ArrayList<Integer> iy=new ArrayList<Integer>();
        for(int i=0; i<COLUMNS;i++){
            ix.add(i)
        }
        for(int i=0; i<ROWS;i++){
            iy.add(i)
        }
        boolean horizontal=true;
        ArrayList<Cell> spiral=new ArrayList<Cell>();
        while()
        
    }
    
    
    //constructeur
    public Cell(int x,int y){
        this.x=Math.floorMod(x, COLUMNS);
        this.y=Math.floorMod(x, ROWS);;
    }
    
    public int x(){
        return x;
    }
    
    public int y(){
        return y;
    }
    
    public int rowMajorIndex(){
        return x+y*ROWS;    //FIXME shouldn't it be COLUMNS?
    }
    
    public Cell neighbor(Direction dir){
        switch(dir){
        case N:
            return new Cell(x,Math.floorMod(y-1,ROWS)); //FIXME do we really need to use floorMod here?
        case S:
            return new Cell(x,Math.floorMod(y+1,ROWS));
        case W:
            return new Cell(Math.floorMod(x-1,COLUMNS),y);
        case E:
            return new Cell(Math.floorMod(x+1,COLUMNS),y);
        }
        throw new Error("quel est cette direction?");   //FIXME maybe return null?
    }
    
    public boolean equals(Object that){
        if (that==null){
            return false;
        }
        if(this.getClass().equals(that.getClass())){
           return this.x==((Cell)that).x() && this.y()==((Cell)that).y();
        }
        return false;
    }
    
    
    public String toString(){
        return "("+x+","+y+")";
    }
}
