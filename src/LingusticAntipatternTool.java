package src;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * The main class used for the detection of the lingustic anti-pattenrs using the parsing of xml
 * Current LAs:
 *      A.2 "Is" Returns more than a boolean
 *      A.3 "Set" - method returns
 *      B.3 "Get" - does not return
 * @created    3/31/21
 * @last_edit  5/8/21
 * @author     Devan Lad <>
 * @author     Stephen Cook <sjc5897@rit.edu>
 */
public class LingusticAntipatternTool {
        static XPath x_path;            //x_path
        static Document doc;            //current_doc
        static Scanner ui_scanner;      //Scanner for user input


    public static void main(String[] args) throws Exception {
        HashMap<String,ArrayList<String>>  violation_map;           //A changing map of the violations for a test
        XPathFactory xpathFactory = XPathFactory.newInstance();     //Creates the XPATH Factory
        x_path = xpathFactory.newXPath();           //Creates the XPATH object
        ui_scanner = new Scanner(System.in);        //creates our scanner object

        //Run forever
        while(true) {

            //Get test xml file from user
            doc = getXMLDocument();
            System.out.println ("INFO: Starting Checks");

            //Run A.2 "Is" returns more than a boolean
            System.out.println ("INFO: Running Check: A.2 \"Is\" returns more than a boolean");
            violation_map = getIsReturnsMoreThanBoolean();
            printTestResult(violation_map);
            System.out.println("INFO: Check Complete: A.2 \"Is\" returns more than a boolean");

            //Run A.3 "Set" - method returns
            System.out.println("INFO: Running Check: A.3 \"Set\" method returns");
            violation_map = getSetMethodReturns();
            printTestResult(violation_map);
            System.out.println("INFO: Check Complete: A.3 \"Set\" method returns");

            System.out.println("INFO: Running Check: B.2 \"Validation\" does no confirm");
            violation_map = getValidationDoesNotConfirm();
            printTestResult(violation_map);
            System.out.println("INFO: Check Complete: A.2 \"Validation\" does no confirm");

            //Run B.3 "Get" method does not return
            System.out.println ("INFO: Running Check: B.3 \"Get\" method does not return");
            violation_map = getNoReturn();
            printTestResult(violation_map);
            System.out.println ("INFO: Check Complete: B.3 \"Get\" method does not return");

            System.out.println ("INFO: All Checks complete");
            if(!getContinue()){
                break;
            }
        }

    }
    /**
     * Function to get the XML file path from the user and create the xml doc
     * @return  document: XML Document
     */
    private static Document getXMLDocument(){
        System.out.println("Enter XML File Path: ");        //request file

        String file_path = ui_scanner.nextLine();           //gets file path from user

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;

        //Try to get doc
        try {
            //trys the filepath
            builder = factory.newDocumentBuilder();
            doc = builder.parse(file_path);
        }

        //If file not found
        catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            return getXMLDocument();
        }
        catch (ParserConfigurationException e ){
            e.printStackTrace();
        }
        catch (SAXException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * Gets user response for wish to continue
     * @return  Boolean, does the user wish to continue
     */
    private static boolean getContinue(){
        System.out.println("Do you wish to continue: (y/n)");
        String resp = ui_scanner.nextLine();
        while(!resp.equals("y") && !resp.equals("n")){
            System.out.println("Invalid input: y/n");
            resp = ui_scanner.nextLine();
        }
        if(resp.equals("y")){
            return true;
        }
        return false;
    }

    /**
     * This method prints the test results in a more human readable fashion
     * @param nodes The violations that occur
     */
    private static void printTestResult(HashMap<String, ArrayList<String>> nodes){
        int total_count = 0;
        //Print the number of antipatterns found
        System.out.println("\tAnti-pattern detected in " + nodes.size() + " file(s):");

        //print the nodes out
        for(String file_name : nodes.keySet()){
            total_count += nodes.get(file_name).size();
            System.out.println("\t\tFile: \"" + file_name + "\" \tNumber of Violations: " + nodes.get(file_name).size());
            String print_str = "\t\t\tViolating Methods: ";
            for(String method_name : nodes.get(file_name)){
                print_str += method_name + ", ";
            }
            System.out.println(print_str);
        }
        System.out.println("\tTotal Violations: " + total_count);
    }

    /**
     * Takes in a string expression and runs the expression. Then parses the return information for relevant info
     * @param str_expr  The xpath expression
     * @return          A HashMap with file names as keys and an array of violating strings as values
     */
    private static HashMap<String,ArrayList<String>> getViolatingMethods(String str_expr){
        //Hash Map to store our information. The Key is the Class, the Array is the list of methods in that string which violate
        //First get all class info
        HashMap<String,ArrayList<String>> violating_methods = new HashMap<>();
        

        try{
            //Compile the expr
            XPathExpression expr = x_path.compile(str_expr);

            //Run expression
            NodeList nodes = (NodeList) expr.evaluate(doc,XPathConstants.NODESET);

            //For each found node
            for(int index = 0; index < nodes.getLength(); index++){

                //Get node
                Node current_node = nodes.item(index);

                //Get parent node
                Node parent_node = current_node.getParentNode();

                //Climb until we reach the parent node
                while (!parent_node.getNodeName().equals("unit")){
                    parent_node = parent_node.getParentNode();
                }

                //Get relevant info
                String file_name = parent_node.getAttributes().getNamedItem("filename").getTextContent();
                //Gets the method name
                String method_name = current_node.getTextContent();

                //Add to hash map
                if(!violating_methods.containsKey(file_name)){
                    violating_methods.put(file_name,new ArrayList<>());
                }
                violating_methods.get(file_name).add(method_name);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        //return list
        return violating_methods;
    }


    /**
     * CHECK A.2: "Is" Returns more than a boolean
     *
     * @return  Hash map of violating strings
     */
    private static  HashMap<String,ArrayList<String>> getIsReturnsMoreThanBoolean() {
        String str_expr = "//function[" +                                //Functions
                "not(contains(annotation/name, 'Test')) and (" +         //Not Noted as tests
                "not(contains(type/name,'boolean') or " +               //And Does not have return type Bool
                "contains(type/name, 'Boolean')))] " +                        //Or Capital Boolean
                "[starts-with(translate(name,'IS','is'),'is')]" +       //and starts with is
                "/name";                                                //get the name
        return getViolatingMethods(str_expr);
    }

    /**
     * CHECK A.3: "Set" method returns
     *
     * @return          List of methods names that violate this rule
     */
    private static HashMap<String,ArrayList<String>> getSetMethodReturns() {
        String str_expr = "//function[" +
                "not(contains(annotation/name, 'Test')) and " +             //Eliminates Test functions
                "not(contains(type/name,'void'))]" +                        //Gets methods are not void
                "[starts-with(translate(name,'SET','set'),'set')]" +          //Gets methods that start with set
                "/name";

        return getViolatingMethods(str_expr);
    }

    /**
     * CHECK B.2: "Get" does not return
     *
     * @return          List of methods names that violate this rule
     */
    private static HashMap<String,ArrayList<String>> getValidationDoesNotConfirm() {
        String str_expr = "//function[" +
                "not(contains(annotation/name,'Test')) and " +
                "not(contains(type/name,'boolean')) and " +
                "not(contains(throws,'throws'))]" +
                "[not(contains(.,'throw'))]" +
                "[not(contains(.,'try')) and " +
                "not(contains(.,'catch'))]" +
                "[(starts-with(translate(name,'VALIDATE','validate'), 'validate')) or " +
                "(starts-with(translate(name,'CHECK','check'),'check')) or " +
                "(starts-with(translate(name,'ENSURE','ensure'),'ensure'))] " +
                "/name";                                                

        return getViolatingMethods(str_expr);
    }

    /**
     * CHECK B.3: "Get" does not return
     *
     * @return          List of methods names that violate this rule
     */
    private static HashMap<String,ArrayList<String>> getNoReturn() {
        String str_expr = "//function[" +                                           //Gets function
                "not(contains(annotation/name,'Test')) and " +                      //That is not test
                "contains(type/name, 'void')]" +                                    //and is void
                "[(starts-with(translate(name,'GET','get'), 'get')) or" +              //That starts with get
                "(starts-with(translate(name,'RETURN','return'),'return'))]" +       //Or return
                "/name";                                                            //the function name

        return getViolatingMethods(str_expr);
    }
}
