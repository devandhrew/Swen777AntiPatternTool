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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
    public static void main(String[] args) throws Exception {
        List<String> nodes;

        //Get test xml file from user
        Document doc = getXMLDocument();

        //Set up XPATH
        XPathFactory xpathFactory = XPathFactory.newInstance();
        // Create XPath object
        XPath xpath = xpathFactory.newXPath();

        //Run A.2 "Is" returns more than a boolean
//        System.out.println ("Running Check: A.2 \"Is\" returns more than a boolean\"");
//        nodes = getIsReturnsMoreThanBool(doc, xpath);
//        printTestResult(nodes);

        //Run A.3 "Set" - method returns
        System.out.println ("Running Check: A.3 \"Set\" method returns\"");
        nodes = getSetMethodReturns(doc, xpath);
        printTestResult(nodes);

        //Run B.3 "Get" method does not return
        System.out.println ("Running Check: B.3 \"Get\" method does not return\"");
        nodes = getNoReturn(doc, xpath);
        printTestResult(nodes);


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
     * @param nodes The names of functions identified as breaking the LA
     */
    private static void printTestResult(List<String> nodes){
        //Print the number of antipatterns found
        System.out.println("Number of Detected Anti-pattern: " + nodes.size());

        //print the nodes out
    }

    private static List<String> getNoReturn(Document doc, XPath xpath) {
        List<String> list = new ArrayList<>();
        try {
            //create XPathExpression object
            XPathExpression expr = xpath.compile("/unit//unit/class/block/function[type/name='void'][starts-with(./name,substring('get',1,3)) or starts-with(./name,substring('return',1,6))]/name");
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
     * This method looks for identifiers with "set" in the name and checks that it is a void return
     * This is an algorithm for rule A.3, "Set" method returns.
     * "A set method having a return type different than void AND not documenting the return type/value
     *  with comments"
     * @param doc       The document (source file xml) being parsed
     * @param xpath     xPath variaable used to assist in the parsing ogf the xml
     * @return          List of methods names that violate the Set method returns
     */
    private static List<String> getSetMethodReturns(Document doc, XPath xpath){
        List<String> violating_strings = new ArrayList<>();      //Storage ret value, returns the list of strings violating the rule
        try{
            //create the XPathExpression Object
            // //function[not(contains(type/name,'void'))][starts-with(name,'set')] <= gets the all functions with set in the name
            // Gets all set methods
            XPathExpression expr = xpath.compile("//function[not(contains(type/name,'void'))][starts-with(name,'set')]");
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            // Get non-void methods
            for (int i = 0; i < nodes.getLength(); i++) {
                String function_name = nodes.item(i).getTextContent();
                //Get the class
                expr = xpath.compile("//class[contains(function,'" + function_name + "')]");
                NodeList nodes2 = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                System.out.print(nodes2);


                violating_strings.add(nodes.item(i).getTextContent());
                }
        }
        // Catch XPathExpression Errors
        catch (XPathExpressionException e){
            e.printStackTrace();
        }
        //Returns the name of violating strings.
        return violating_strings;
    }
}
