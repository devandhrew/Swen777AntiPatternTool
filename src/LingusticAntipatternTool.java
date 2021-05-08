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
 *      A.1 "Get" - more than accessor
 *      //TODO: A.2 "Is" Returns more than a boolean
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

    public static void main(String[] args) throws Exception {
        HashMap<String,ArrayList<String>>  violation_map;           //A changing map of the violations for a test
        XPathFactory xpathFactory = XPathFactory.newInstance();     //Creates the XPATH Factory
        x_path = xpathFactory.newXPath();                           //Creates the XPATH object

        //Run forever
        while(true) {

            //Get test xml file from user
            doc = getXMLDocument();


            //Run A.2 "Is" returns more than a boolean
            //        System.out.println ("Running Check: A.2 \"Is\" returns more than a boolean\"");
            //        nodes = getIsReturnsMoreThanBool(doc, xpath);
            //        printTestResult(nodes);

            //Run A.3 "Set" - method returns
            System.out.println("INFO: Running Check: A.3 \"Set\" method returns");
            violation_map = getSetMethodReturns();
            printTestResult(violation_map);
            System.out.println("INFO: Check Complete: A.3 \"Set\" method returns");

            //        //Run B.3 "Get" method does not return
            //        System.out.println ("Running Check: B.3 \"Get\" method does not return");
            //        nodes = getNoReturn(doc, xpath);
            //        printTestResult(nodes);

            //TODO: Get if user wishes to continue
        }

    }
    /**
     * Function to get the XML file path from the user and create the xml doc
     * @return  document: XML Document
     */
    private static Document getXMLDocument(){
        Scanner ui_scanner = new Scanner(System.in);        //creates our scanner object
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

    private static List<String> getIsReturnsMoreThanBoolean(Document doc, XPath xpath) {
        List<String> list = new ArrayList<>();
        try {
            //create XPathExpression object
            XPathExpression expr = xpath.compile("//function[not(contains(type/name,'boolean') or contains(type/name, 'void'))][starts-with(name,'is')]/name");
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getTextContent());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
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
     * This method looks for identifiers with "set" in the name and checks that it is a void return
     * This is an algorithm for rule A.3, "Set" method returns.
     * "A set method having a return type different than void AND not documenting the return type/value
     *  with comments"
     * @param doc       The document (source file xml) being parsed
     * @param xpath     xPath variaable used to assist in the parsing ogf the xml
     * @return          List of methods names that violate the Set method returns
     */
    private static HashMap<String,ArrayList<String>> getSetMethodReturns() {
        HashMap<String,ArrayList<String>> violating_methods;
        String str_expr = "//function[" +
                "not(contains(annotation/name, 'Test')) and " +             //Eliminates Test functions
                "not(contains(type/name,'void'))]" +                        //Gets methods are not void
                "[starts-with(translate(name,'SET','set'),'set')]" +          //Gets methods that start with set
                "/name";

        violating_methods =  getViolatingMethods(str_expr);
        return violating_methods;
    }
}
