package src.test;

import java.util.concurrent.ExecutionException;

public class validation {
    //10 invalid methods
    //1
    //"maven-core/src/main/java/org/apache/maven/DefaultMaven.java"
    private void validatePrerequisitesForNonMavenPluginProjects(){

    }
    //2
    //maven-artifact/src/test/java/org/apache/maven/artifact/versioning/VersionRangeTest.jav
    public int checkInvalidRange(){
        return 1;
    }

    //3
    //from replication kit
    public void checkCollision(){

    }

    //4
    //from replication kit
    private void checkRecordingFile(){

    }

    //5
    //from replication kit
    private void checkVertices(){}


    //Created method edge cases
    //6
    //Created method, detects all capitals
    public int CHECK_Valid(){
        return 1;
    }

    //7
    //created method, simple name
    public int check(){
        return 1;
    }

    //8
    //created method, underscore
    public int validate_number(){
        return 1;
    }

    //9
    //created method, hungarian notation
    public int iEnsureValid(){
        return 1;
    }

    //10
    //method created, word twice in function
    public int validateChecker(){
        return 1;
    }

    //10 valid methods

    //1'
    //"maven-master/maven-core/src/main/java/org/apache/maven/plugin/internal/DefaultMavenPluginManager.java
    public void checkRequiredMavenVersion() throws Exception{

    }
    //2
    // "maven-master/maven-core/src/main/java/org/apache/maven/plugin/internal/DefaultMavenPluginManager.java
    private void validateParameters () throws Exception{

    }

    //3
    //"maven-master/maven-core/src/main/java/org/apache/maven/plugin/internal/DefaultMavenPluginManager.java
    public boolean validateFileModel(){
        return true;

    }

    //4
    //"maven-master/maven-core/src/main/java/org/apache/maven/lifecycle/internal/MojoExecutor.java"
    public void ensureDependenciesAreResolved() throws Exception{

    }
    //5
    //maven-master/maven-model-builder/src/test/java/org/apache/maven/model/validation/DefaultModelValidatorTest.java
    private boolean validateRaw( String pom )
            throws Exception{
        return true;
    }
    //Created method edge cases

    //6
    //created method, starts with word
    public int checkersCount(){
        return 1;
    }

    //7
    //created method, underscore
    public boolean check_valid(){
        return true;
    }

    //8
    //created method, hungarian notation
    public boolean bCheckValid(){
        return true;
    }

    //9
    //created method, does not start, but contains string
    public int countCheckers(){
        return 1;
    }

    //10
    //method created, word twice in function
    public boolean checkCheckers() {
        return true;
    }

    //11
    //checks expetion type
    public void checkError(){
        try{

        }
        catch (Exception e){

        }
    }



}
