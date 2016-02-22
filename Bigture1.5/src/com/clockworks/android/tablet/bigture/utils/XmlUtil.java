package com.clockworks.android.tablet.bigture.utils;

import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtil
{
	public static String getElementName(Element element)
	{
		if (element.getNodeType() == Element.ELEMENT_NODE)
			return element.getNodeName();
		else
			return null;
	}
	
	public static Element getFirstChildElement(Element element)
	{
		Element childElement = null;
		NodeList nodeList = element.getChildNodes();
		
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			if(node.getNodeType() == Element.ELEMENT_NODE)
			{
				childElement = (Element)node;
				break;
			}
		}
		
		return childElement;
	}
	
	public static Element getNextSiblingElement(Element element)
	{
		Element resultElement = null;
		
		try
		{
			Node node = element.getNextSibling();
			while(node != null)
			{
				if(node.getNodeType() == Element.ELEMENT_NODE)
				{
					resultElement = (Element)node;
					break;
				}
				node = node.getNextSibling();
			}
		}
		catch(Throwable t)
		{
			//t.printStackTrace();
			resultElement = null;
		}
		return resultElement;
	}
	
	public static HashMap<String,String> getElementAttributes(Element element)
	{
		HashMap<String,String> attributes = new HashMap<String, String>();
		
		NamedNodeMap map = element.getAttributes();
		for (int i = 0; i < map.getLength(); i++)
		{
			Node node = map.item(i);
			if (node.getNodeType() == Node.ATTRIBUTE_NODE)
				attributes.put(node.getNodeName(), node.getNodeValue());
		}
		
		return attributes;
	}
	/**
	 * getNodeAttribute
	 *   ���� ���õ� ����� �Ӽ����� ��� �ǵ�����.
	 * @param node
	 * @param name
	 * @return
	 */
	public static String getNodeAttribute(Node node, String name)
	{
		String value = null; 
		try
		{
			NamedNodeMap nodeMap = node.getAttributes(); 
			Node nodeAttr = nodeMap.getNamedItem(name);
			value = nodeAttr.getNodeValue();
		}
		catch(Exception e)
		{
			value = null;
		}
		
		return value;
	}
	
	public static String getNodeValue(Node node)
	{
		String value = "";
		
		try
		{
			if(node.getFirstChild() != null)
				value = node.getFirstChild().getNodeValue();
		}
		catch(Exception e)
		{
			value = null;
		}
		
		return value;
	}
	
	public static long getNodeLongValue(Node node, long defaultValue)
	{
		long value = defaultValue;
		
		try
		{
			if(node.getFirstChild() != null)
				value = Long.parseLong(node.getFirstChild().getNodeValue());
		}
		catch(Exception e)
		{
			value = defaultValue;
		}
		
		return value;
	}

	public static long getNodeIntValue(Node node, int defaultValue)
	{
		int value = defaultValue;
		
		try
		{
			if(node.getFirstChild() != null)
				value = Integer.parseInt(node.getFirstChild().getNodeValue());
		}
		catch(Exception e)
		{
			value = defaultValue;
		}
		
		return value;
	}

	public static int toInt(String text, int defaultVal)
	{
		int value = defaultVal;
		
		try
		{
			value = Integer.parseInt(text.trim());
		}
		catch(Exception e)
		{
			
		}
		
		return value;
	}
	
	public static String toCDATA(String text)
	{
		return "<![CDATA[" + text + "]]>";
	}
}
