package src;

import org.w3c.dom.*;

import javax.xml.parsers.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The main class used for the detection of the lingustic anti-pattenrs using the parsing of xml
 * Current LAs:
 *      A.1 "Get" - more than accessor
 *      //TODO: A.2 "Is" Returns more than a boolean
 *      A.3 "Set" - method returns
 * @created    3/31/21
 * @last_edit  4/5/21
 * @author     Devan Lad <>
 * @author     Stephen Cook <sjc5897@rit.edu>
 */
public class LingusticAntipatternTool {
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse("test.xml");
            // Create XPathFactory object
            XPathFactory xpathFactory = XPathFactory.newInstance();

            // Create XPath object
            XPath xpath = xpathFactory.newXPath();

            List<String> nodes = getNoReturn(doc, xpath);
            System.out.println(nodes.size());
            System.out.println(Arrays.toString(nodes.toArray()));

            nodes = getSetMethodReturns(doc, xpath);
            System.out.println(nodes.size());
            System.out.println(Arrays.toString(nodes.toArray()));




        } catch (Exception e) {
            e.printStackTrace();
        }

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
            XPathExpression expr = xpath.compile("//function[not(contains(type/name,'void'))][starts-with(name,'set')]/name");
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            // Get non-void methods
            for (int i = 0; i < nodes.getLength(); i++) {
                // Check if void function
                // TODO: This should be done using xpath, this would likely trigger if the function has void in the name
                // ^ Potental antipattern?{
                //TODO: As part of this LA, comments should be checked
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
