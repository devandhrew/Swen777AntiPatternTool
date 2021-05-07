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
 *      //TODO: A.2 "Is" Returns more than a boolean
 *      A.3 "Set" - method returns
 *      B.3 "Get" - does not return
 * @created    3/31/21
 * @last_edit  4/5/21
 * @author     Devan Lad <>
 * @author     Stephen Cook <sjc5897@rit.edu>
 */
public class LingusticAntipatternTool {
        static XPath x_path;
        static Document doc;
        static NodeList unit_list;


    public static void main(String[] args) throws Exception {
        HashMap<String,ArrayList<String>>  violation_map;           //A changing map of the violations for a test
        XPathFactory xpathFactory = XPathFactory.newInstance();     //Creates the XPATH Factory
        x_path = xpathFactory.newXPath();                      //Creates the XPATH object

        //Run forever
        while(true) {

            //Get test xml file from user
            doc = getXMLDocument();

            //as part of setup
            System.out.println("INFO: Document found, Doing Initial Parse");
            getUnitNotes();
            System.out.println("INFO: Parsing Complete");


            //Run A.2 "Is" returns more than a boolean
            //        System.out.println ("Running Check: A.2 \"Is\" returns more than a boolean\"");
            //        nodes = getIsReturnsMoreThanBool(doc, xpath);
            //        printTestResult(nodes);

            //Run A.3 "Set" - method returns
            System.out.println("INFO: Running Check: A.3 \"Set\" method returns");
            violation_map = getSetMethodReturns();
            printTestResult(violation_map);

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
        //TODO: Edit to fit HashMap Better
        //Print the number of antipatterns found
        System.out.println("Number of Detected Anti-pattern: " + nodes.size());

        //print the nodes out
        for(String class_name : nodes.keySet()){
            if( nodes.get(class_name).size() == 0){
                break;
            }
            String print_str = "\t";
            System.out.println(class_name);
            for(String method_name : nodes.get(class_name)){
                print_str += method_name + ", ";
            }
            System.out.println(print_str);
        }
    }

    /**
     * Does an intial parse of the document getting all of the unit nodes
     */
    private static NodeList getUnitNotes(){
        String expr_str = "//unit/unit[@filename]";                 //the expression string
        try{
            //create xpath to get all units
            //Evaluate and set unit note
            unit_list =  (NodeList) x_path.evaluate(expr_str,doc,XPathConstants.NODESET);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static HashMap<String,ArrayList<String>> getViolatingMethods(String str_expr){
        //Hash Map to store our information. The Key is the Class, the Array is the list of methods in that string which violate
        //First get all class info
        HashMap<String,ArrayList<String>> violating_methods = new HashMap<>();
        try {
            //Compile the expr
            XPathExpression expr = x_path.compile(str_expr);
            //Iterate through the unit nodes
            for (int index = 0; index < unit_list.getLength(); index++) {

                Element currentNode = (Element) unit_list.item(index);
                //Evaluate the expression on the unit_note
                NodeList nodes = (NodeList) expr.evaluate(currentNode,XPathConstants.NODESET);

                //If we have a result
                if (nodes.getLength() > 0) {
                    //add to violate methods hash_map
                    String node = unit_list.item(index).getAttributes().getNamedItem("filename").toString();
                    violating_methods.put(node, new ArrayList<>());
                    for (int index_2 = 0; index_2 < nodes.getLength(); index_2++) {
                        violating_methods.get(node).add(nodes.item(index_2).getTextContent());
                    }
                }
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
     * @return          List of methods names that violate the Set method returns
     */
    private static HashMap<String,ArrayList<String>> getSetMethodReturns() {
        HashMap<String,ArrayList<String>> violating_methods = new HashMap<>();
        String str_expr = "class/block/function[" +
                "not(contains(annotation/name, 'Test')) and " +             //Eliminates Test functions
                "not(contains(type/name,'void'))]" +                        //Gets methods are not void
                "[starts-with(translate(name,'SET','set'),'set')]"+          //Gets methods that start with set
                "/name";

        violating_methods =  getViolatingMethods(str_expr);
        return violating_methods;
    }
}
