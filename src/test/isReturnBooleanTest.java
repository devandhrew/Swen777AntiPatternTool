package src.test;

import com.sun.jdi.Mirror;

public class isReturnBooleanTest {
    //10 invalid methods

    //1
    //From replication kit
    public int isValid(){
        return 1;
    }

    //2
    //from replication kit
    public String isDependencyResolutionRequired(){
        return "yes";
    }


    //Created method edge cases
    //6
    //Created method, detects all capitals
    public int ISFREE(){
        return 1;
    }

    //7
    //created method, simple name
    public int is(){
        return 1;
    }

    //8
    //created method, underscore
    public int is_one(){
        return 1;
    }

    //9
    //created method, hungarian notation
    public int vIsOne(){
        return 1;
    }

    //10
    //method created, word twice in function
    public int isIssue(){
        return 1;
    }




    //10 valid methods

    //1
    //Apache/maven -> maven-compact/src/main/java/org/apache/maven/repository/DefaultMirrorSelector.java
    static boolean isExternalRepo(){
        return true;
    }

    //2
    //Apache/maven -> maven-compact/src/main/java/org/apache/maven/repository/DefaultMirrorSelector.java
    private static boolean isLocal(){
        return true;
    }

    //3
    //Apache/maven -> maven-compact/src/main/java/org/apache/maven/repository/DefaultMirrorSelector.java
    static boolean isExternalHttpRepo(){
        return true;
    }

    //4
    //apache/tomcat -> tomcat/java/org/apache/tomcat/JarScanner.java
    public boolean isSkipAll(){
        return true;
    }

    //5
    //apache/tomcat -> tomcat/java/org/apache/util.digester/Digester.java
    public static boolean isGeneratedCodeLoaderSet(){
        return true;
    }


    //Created method edge cases
    //6
    //created method, starts with word
    public int issueWarning(){
        return 1;
    }

    //7
    //created method, underscore
    public boolean is_warning(){
        return true;
    }

    //8
    //created method, hungarian notation
    public Boolean bIsWarning(){
        return true;
    }

    //9
    //created method, does not start, but contains string
    public String restoreIssue(){
        return "Settle";}

    //10
    //method created, word twice in function
    public boolean isIssued() {
        return true;
    }





}
