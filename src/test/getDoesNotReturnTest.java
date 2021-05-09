package src.test;

import com.sun.jdi.Mirror;

import java.util.HashMap;
import java.util.Map;

public class getDoesNotReturnTest {
    //10 invalid methods

    //1
    //from replication kit
    private void getUpstreamProducts(){

    }

    //2
    //from replication kit
    private void getTaskAttributes(){

    }

    //3
    //from replication kit
    private void returnNumber(){
    }

    //4
    //from replication kit
    private  void returnError(){

    }
    //5
    //from replication kit
    private void getNewClassRoom(){

    }

    //Created method edge cases

    //6
    //Created method, detects all capitals
    public void GETTime(){
    }

    //7
    //created method, simple name
    public void get1(){

    }

    //8
    //created method, underscore
    public void get_two(){

    }

    //9
    //created method, hungarian notation
    public void iGetTwo(){

    }

    //10
    //method created, word twice in function
    public void getGetting(){
    }


    //10 valid methods

    //1
    //from replication kit
    public String getPath(){
        return "Path";
    }

    //2
    //Apache/maven -> maven-compact/src/main/java/org/apache/maven/DefaultMaven.java
    private Map<String, Object> getProjectMap(){
        return new HashMap<>();
    }

    //3
    //Apache/maven -> maven-compact/src/main/java/org/apache/mave/repository/DefaultMirrorSelector.java
    public Mirror getMirror(){
        return null;
    }

    //4
    //apache/tomcat -> tomcat/java/org/apache/util.digester/Digester.java
    public int getCount(){
        return 1;
    }

    //5
    //Apache/maven -> maven-compact/src/main/java/org/apache/maven/plugin/DefaultPluginDescriptorCache.java
    public int get(){
        return 1;
    }


    //6
    //created method, starts with word
    public String getawayMessage(){
        return "bye";
    }

    //7
    //created method, underscore
    public int get_one(){
        return 1;
    }

    //8
    //created method, hungarian notation
    public int iGetOne(){
        return 1;
    }

    //9
    //created method, does not start, but contains string
    public String whoIsGettingAway(){
        return "a";
    }

    //10
    //method created, word twice in function
    public int getGetter(){
        return 1;
    }


}
