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
	private ConcurrentHashMap<String, HadoopTemplate> templates = new ConcurrentHashMap<String, HadoopTemplate>();
	private ConcurrentHashMap<String, HadoopFile> files = new ConcurrentHashMap<String, HadoopFile>();
	private ConcurrentHashMap<String, AbstractAdaptor> adaptors = new ConcurrentHashMap<String, AbstractAdaptor>();

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

	public void initializeServices(Document document) throws TransformerException {
		createTemplates(document);
	}

	public void createTemplates(Document document) throws TransformerException {

		Element templatesElement = XmlUtils.findNode(document, "//templates");
		NodeList templateNodes = XPathAPI.selectNodeList(templatesElement, "template");
		logger.info(templateNodes.getLength() + " template found.");

		for (int i = 0; i < templateNodes.getLength(); i++) {
			Element templateElement = (Element) templateNodes.item(i);

			HadoopTemplate hadoopTemplate = new HadoopTemplate();
			hadoopTemplate.setId(templateElement.getAttribute("id"));
			hadoopTemplate.setTemplateName(templateElement.getAttribute("name"));
			
			//hadoop file okuyalim.
			NodeList files = XPathAPI.selectNodeList(templateElement, "/file");

			
			//paramslari okuyalim.
			HashMap<String, String> params = new HashMap<>();
			NodeList paramList = XPathAPI.selectNodeList(templateElement, "//param");
			for (int j = 0; j < paramList.getLength(); j++) {
				Element param = (Element) paramList.item(j);
				if (!param.getAttribute("name").equalsIgnoreCase("dataLength")) {
					params.put(param.getAttribute("name"), param.getAttribute("value"));
				} else {
					int length = Integer.valueOf(param.getAttribute("value"));
					NodeList lineParams = XPathAPI.selectNodeList(templateElement, "//param");

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

			templates.put(hadoopTemplate.getId(), hadoopTemplate);
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