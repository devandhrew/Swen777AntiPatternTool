package src.test;

import javax.management.ObjectName;
import java.awt.*;
import java.util.*;
import java.util.List;

public class setReturnTest {

    //Ten discovered invalid methods

    //1
    //Violating method from replication package
    public synchronized Map setConnectionAsSharingClient(Map map){
        return new HashMap<String,String>();
    }

    //2
    //Violating method from replication package
    private double[] setSubspaceInternal(){
        double[] f = {1.1};
        return f;
    }

    //3
    //Violating method from replication package
    public String setAnimationView(){
        return "AnimationView";
    }

    //4
    //Violating method from replication package
    public Dimension setBreadth(Dimension target, int source){
        return target;
    }

    //5
    //Violating method from replication package
    public List<Object> set_keygen_ctx(){
        return new ArrayList<>();
    }




    //Created method edge cases
    //6
    //Created method, detects all capitals
    public int SET_free(){
        return 1;
    }

    //7
    //created method, simple name
    public int set(){
        return 1;
    }

    //8
    //created method, underscore
    public int set_one(){
        return 1;
    }

    //9
    //created method, hungarian notation
    public int vSetOne(){
        return 1;
    }

    //10
    //method created, word twice in function
    public int setSettlers(){
        return 1;
    }



    //Ten valid methods

    //1
    //apache/tomcat -> tomcat/java/org/apache/tomcat/JarScanner.java
    public void setJarScanFilter(){

    }
    //2
    //apache/tomcat -> tomcat/java/org/apache/util.digester/Digester.java
    public static void setGeneratedCodeLoader(){

    }
    //3
    //apache/tomcat -> tomcat/java/org/apache/util.digester/Digester.java
    public void setKnown(){

    }

    //4
    //apache/tomcat -> tomcat/java/org/apache/el/parser/AstDotSuffix.java
    public void setImage(){}

    //5
    //apache/tomcat -> tomcat/java/org/apache/el/parser/Node.java
    public void setValue(){
    }



    //Created method edge cases
    //6
    //created method, starts with word
    public int settlersCount(){
        return 1;
    }

    //7
    //created method, underscore
    public void set_two(){
    }

    //8
    //created method, hungarian notation
    public void vSetTwo(){
    }

    //9
    //created method, does not start, but contains string
    public String restoreSettlers(){
    return "Settle";}

    //10
    //method created, word twice in function
    public void setSettlement() {
    }


}
