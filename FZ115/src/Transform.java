import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import javax.swing.text.Document;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;
import org.cyberneko.html.parsers.DOMParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class Transform {
	
	public static String getDoc( InputStream is ) throws IOException
	{
		WordExtractor extractor = new WordExtractor(is);
		return extractor.getText();
		
	}
	
	public static String getDocx( String filename ) throws IOException, XmlException, OpenXML4JException
	{
		OPCPackage opc = POIXMLDocument.openPackage(filename);
		POIXMLTextExtractor docx = new XWPFWordExtractor(opc);
		return docx.getText();
	}
	
	public static void main(String[] args) throws IOException, XmlException, OpenXML4JException, SAXException {
		String path = "/home/junco/grape";
		File dir = new File(path);
		File[] file = dir.listFiles();
		for( int i=0; i<file.length; i++ )
		{
			if( !file[i].isDirectory() )
			{
				//解析doc,docx格式文件
				if( file[i].toString().matches(".*\\.docx?"))
				{
					String[] tem = file[i].toString().split("\\.");
					FileWriter fw = new FileWriter(tem[0]+".txt");
					//解析doc格式文件
					if( file[i].toString().endsWith(".doc"))
					{
						InputStream is = new FileInputStream(file[i].toString());
						fw.write(getDoc(is));
					}
					//解析docx格式文件
					if( file[i].toString().endsWith(".docx"))
					{
						fw.write(getDocx(file[i].toString()));	
					}
					fw.close();
				}
				if( file[i].toString().matches(".*\\.pdf"))
				{
					String[] tem = file[i].toString().split("\\.");
					FileWriter fw = new FileWriter(tem[0]+".txt");
					PDDocument pdf = PDDocument.load(file[i]);
					PDFTextStripper stripper = new PDFTextStripper();
					fw.write(stripper.getText(pdf));
					fw.close();
				}

			}
		}
	}
}
