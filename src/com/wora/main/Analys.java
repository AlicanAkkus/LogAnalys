package com.wora.main;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.wora.adaptor.AbstractAdaptor;
import com.wora.bean.LineBean;
import com.wora.db.AbstractDBService;
import com.wora.db.DerbyDAOImp;
import com.wora.file.HadoopFile;
import com.wora.template.HadoopTemplate;
import com.wora.util.XmlUtils;

public class Analys {

	Logger logger;
	AbstractDBService dbService;
	private static Analys instance;
	private ConcurrentHashMap<String, HadoopTemplate> templatesPool = new ConcurrentHashMap<String, HadoopTemplate>();
	private ConcurrentHashMap<String, HadoopFile> filesPool = new ConcurrentHashMap<String, HadoopFile>();
	private ConcurrentHashMap<String, AbstractAdaptor> adaptorsPool = new ConcurrentHashMap<String, AbstractAdaptor>();

	public Analys() {
		DOMConfigurator.configure("templates/log4j.xml");
		logger = Logger.getLogger(Analys.class);
		logger.debug("Log4j intitialized..");
	}

	public static Analys getInstance() {

		if (instance == null) {
			instance = new Analys();
		}

		return instance;
	}

	public AbstractDBService getDbService() {
		return dbService;
	}

	public static void main(String[] args) throws Exception {

		String fileName = null;
		if (args.length == 1) {
			fileName = args[0];
		} else {
			fileName = "LogAnalys.xml";
		}

		Document document = XmlUtils.loadXmlFromFile(fileName);

		Analys.getInstance().initializeServices(document);
	}

	public void initializeServices(Document document) throws Exception {
		createFiles(document);
		createDestinations(document);
		createTemplates(document);
	}

	public void createFiles(Document document) throws Exception {
		Element filesElement = XmlUtils.findElement(document, "//files");
		NodeList files = XPathAPI.selectNodeList(filesElement, "file");
		logger.info(files.getLength() + " found in a configüration.");

		for (int i = 0; i < files.getLength(); i++) {
			Element element = (Element) files.item(i);
			HadoopFile hadoopFile = new HadoopFile();
			hadoopFile.setId(element.getAttribute("id"));

			NodeList params = XPathAPI.selectNodeList(element, "param");
			for (int j = 0; j < params.getLength(); j++) {
				Element paramElement = (Element) params.item(j);

				if ("fileName".equalsIgnoreCase(paramElement.getAttribute("name"))) {
					hadoopFile.setFileName(paramElement.getAttribute("value"));
				}
				if ("fileLocation".equalsIgnoreCase(paramElement.getAttribute("name"))) {
					hadoopFile.setHdfsLocation(paramElement.getAttribute("value"));
				}
			}

			filesPool.put(hadoopFile.getId(), hadoopFile);
		}
	}

	public void createDestinations(Document document) throws Exception {
		// <destinations>
		// <destination id="fileDestination" class="com.wora.adaptor.FileAdaptor">
		// <param name="fileName" value="MyLogAnalysis" />
		// <param name="fileExtension" value="DD-MMM-YYYY" />
		// <param name="exportType" value="xml" />
		// <param name="fileSize" value="100MB" />
		// <param name="fileLocation" value="/home/wora/Analysis" />
		// </destination>
		// <destination id="dbDestination" class="com.wora.adaptor.DbAdaptor">
		// <param name="dbDriver" value="com.postgresql.Driver" />
		// <param name="dbUrl" value="jdbc:postgresql://localhost:5432/Analysis" />
		// <param name="dbUser" value="wora" />
		// <param name="dbPassword" value="wora" />
		// </destination>
		// </destinations>
		
		Element destinations =  XmlUtils.findNode(document, "//destinations");
		NodeList destinationsNodes = XPathAPI.selectNodeList(destinations, "destination");
		logger.info(destinationsNodes.getLength() + " destinations found.");

		for(int i = 0; i < destinationsNodes.getLength(); i++){
			Element destination = (Element) destinationsNodes.item(i);
			String destionationID = destination.getAttribute("id");
			
			AbstractAdaptor adaptor = (AbstractAdaptor)Class.forName(destination.getAttribute("class")).newInstance();
			adaptor.init(destination);
			
			adaptorsPool.put(destionationID, adaptor);
		}
		
		
	}

	public void createTemplates(Document document) throws Exception {

		Element templatesElement = XmlUtils.findNode(document, "//templates");
		NodeList templateNodes = XPathAPI.selectNodeList(templatesElement, "template");
		logger.info(templateNodes.getLength() + " template found.");

		for (int i = 0; i < templateNodes.getLength(); i++) {
			Element templateElement = (Element) templateNodes.item(i);

			HadoopTemplate hadoopTemplate = new HadoopTemplate();
			hadoopTemplate.setId(templateElement.getAttribute("id"));
			hadoopTemplate.setTemplateName(templateElement.getAttribute("name"));

			// hadoop file okuyalim.
			NodeList files = XPathAPI.selectNodeList(document, "//templates/template[@id = '"+ hadoopTemplate.getId() +"']/file");
			if (files.getLength() > 1 || files.getLength() == 0) {
				throw new RuntimeException("Only one file is allowed in template!");
			}
			Element fileElement = (Element) files.item(0);
			String fileSource = fileElement.getAttribute("source");
			if (filesPool.containsKey(fileSource)) {
				hadoopTemplate.setHadoopFile(filesPool.get(fileSource));
			} else {
				throw new RuntimeException("Defined file not found in files pool!");
			}

			// hadoop destinationlari okuyalim.
			NodeList destioanitons = XPathAPI.selectNodeList(templateElement, "//templates/template[@id = '"+ hadoopTemplate.getId() +"']/destination");
			if (files.getLength() == 0) {
				throw new RuntimeException("Minimum one destinations required for template!");
			}
			LinkedList<AbstractAdaptor> adaptors = new LinkedList<AbstractAdaptor>();
			for(int ii=0; ii<destioanitons.getLength(); ii++){
				Element destinationElement = (Element) destioanitons.item(ii);
				String destSource = destinationElement.getAttribute("source");
				if (adaptorsPool.containsKey(destSource)) {
					adaptors.add(adaptorsPool.get(destSource));
				} else {
					throw new RuntimeException("Defined destination not found in destinations pool!");
				}
			}
			hadoopTemplate.setAdaptors(adaptors);
			
			
			// paramslari okuyalim.
			HashMap<String, String> params = new HashMap<>();
			NodeList paramList = XPathAPI.selectNodeList(templateElement,"//templates/template[@id = '"+ hadoopTemplate.getId() +"']/param" );
			for (int j = 0; j < paramList.getLength(); j++) {
				Element param = (Element) paramList.item(j);
				if (!param.getAttribute("name").equalsIgnoreCase("dataLength")) {
					params.put(param.getAttribute("name"), param.getAttribute("value"));
				} else {
					int length = Integer.valueOf(param.getAttribute("value"));
					NodeList lineParams = XPathAPI.selectNodeList(templateElement, "//templates/template[@id = '"+ hadoopTemplate.getId() +"']/param/param");

					if (length != lineParams.getLength()) {
						throw new RuntimeException("Number of lengt and params not equals! Lengt : " + length + " and params length : "
								+ lineParams.getLength());
					}

					LinkedList<LineBean> dataLine = new LinkedList<>();
					for (int k = 0; k < lineParams.getLength(); k++) {
						// <param name="dateOfData" sequence="1" description="log data date" />
						Element lineParam = (Element) lineParams.item(k);
						dataLine.add(new LineBean(lineParam.getAttribute("name"), lineParam.getAttribute("description"), Integer.valueOf(lineParam
								.getAttribute("sequence"))));
					}
					hadoopTemplate.setDataLine(dataLine);

				}
			}

			templatesPool.put(hadoopTemplate.getId(), hadoopTemplate);
		}

	}

	public void initializeDbService() {
		logger.debug("initializeDbService is started.");

		try {
			dbService = new DerbyDAOImp();
			// dbService.init(properties);
			dbService.checkDbMetaData();
		} catch (Exception e) {
			logger.error(e, e);
		}

		logger.debug("initializeDbService is finished.");
	}

	public Properties createPropsFile(String fileName) {

		logger.debug("Properties file creating is started.");

		if (StringUtils.isNotBlank(fileName)) {

			try {
				File propFile = new File(fileName);

				Properties properties = new Properties();
				properties.load(new FileInputStream(propFile));

				logger.debug("Properties file is created.");
				return properties;
			} catch (Exception e) {
				logger.error(e, e);
			}

		}

		return null;

	}

	public void createJob(String jobName, String args[]) {
		logger.debug("createJob is started, job name : " + jobName);

		try {

			Configuration conf = new Configuration();

			Job job = Job.getInstance(conf, jobName);
			job.setJarByClass(Analys.class);
			job.setMapperClass(LogMapper.class);
			job.setCombinerClass(LogReduce.class);
			job.setReducerClass(LogReduce.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			FileInputFormat.addInputPath(job, new Path(args[0]));
			FileOutputFormat.setOutputPath(job, new Path(args[1]));

			// MultipleOutputs.addNamedOutput(job, LogAnalysConstant.METHOD_OUTPUT_NAME, TextOutputFormat.class, NullWritable.class, Text.class);
			// MultipleOutputs.addNamedOutput(job, LogAnalysConstant.VARIABLE_OUTPUT_NAME, TextOutputFormat.class, NullWritable.class, Text.class);
			// MultipleOutputs.addNamedOutput(job, LogAnalysConstant.INITALIZE_OUTPUT_NAME, TextOutputFormat.class, NullWritable.class, Text.class);
			// MultipleOutputs.addNamedOutput(job, LogAnalysConstant.DESTROY_OUTPUT_NAME, TextOutputFormat.class, NullWritable.class, Text.class);
			// MultipleOutputs.addNamedOutput(job, LogAnalysConstant.ANALYS_OUTPUT_NAME, TextOutputFormat.class, NullWritable.class, Text.class);
			// MultipleOutputs.addNamedOutput(job, LogAnalysConstant.UNKNOW_OUTPUT_NAME, TextOutputFormat.class, NullWritable.class, Text.class);

			System.exit(job.waitForCompletion(true) ? 0 : 1);

		} catch (Exception e) {
			logger.error(e, e);
		}

	}

}