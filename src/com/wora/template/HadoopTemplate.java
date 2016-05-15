package com.wora.template;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.log4j.Logger;

import com.wora.adaptor.AbstractAdaptor;
import com.wora.bean.LineBean;
import com.wora.constant.IOConstant;
import com.wora.file.HadoopFile;
import com.wora.main.LogMapper;
import com.wora.util.IOUtils;
import com.wora.util.TemplateValidationProcessor;

/**
 * @author wora
 * @version 2.0
 * */
public class HadoopTemplate implements Runnable {
	private Logger logger = Logger.getLogger(HadoopTemplate.class);
	String id;
	String templateName;
	HashMap<String, String> params;
	HadoopFile hadoopFile;
	LinkedList<LineBean> dataLine;
	LinkedList<AbstractAdaptor> adaptors;

	@Override
	public void run() {

	}

	public class LogMapper extends Mapper<LongWritable, Text, Text, IntWritable> implements IOConstant {

		Logger logger = Logger.getLogger(LogMapper.class);
		private final IntWritable one = new IntWritable(1);

		private Text word = new Text();

		@Override
		public void map(LongWritable key, Text value, Mapper.Context context) throws IOException, InterruptedException {

			char startOfText = params.get("startChar").toCharArray()[0];
			char endOfText = params.get("endChar").toCharArray()[0];

			String data = value.toString();
			int beginIndex = data.indexOf(startOfText), endIndex = data.indexOf(endOfText);

			// logger daki ilgilendigimiz kisimlari alalim sadece.
			if (beginIndex != -1 && endIndex != -1 && beginIndex != endOfText) {
				data = data.substring(beginIndex, endIndex + 1);

				logger.debug("Mapping set : " + data);
				word.set(data);
				context.write(word, one);
			} else {
				// TODO ilgilendigimiz kisimlar yoksa ne yapsin?
				String pattern = params.get("pattern");

				if (data.matches(pattern)) {
					// pattern regex match
				}
			}
		}
	}

	public class LogReduce extends Reducer<Text, IntWritable, Text, IntWritable> implements IOConstant {

		Logger logger = Logger.getLogger(LogReduce.class);
		private IntWritable result = new IntWritable(1);
		MultipleOutputs<Text, Text> output;

		@Override
		public void setup(Context context) {
			output = new MultipleOutputs(context);
		}

		@Override
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			// gelen text line'i dosyaya yazalım.
			try {

				// parse.
				// add mesage to adaptor

			} catch (Exception e) {
				logger.error(e, e);
			}

		}

		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {
			output.close();
			logger.debug("Cleanup context..");
		}

		String generateFileName(Text key) {
			logger.debug("Dosya adi : " + key.toString() + "" + System.currentTimeMillis());
			return key.toString() + "" + System.currentTimeMillis();
		}

	}

	class MultiFileOutput extends MultipleTextOutputFormat<Text, Text> {
		@Override
		protected String generateFileNameForKeyValue(Text key, Text value, String name) {
			return key.toString();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}

	public LinkedList<LineBean> getDataLine() {
		return dataLine;
	}

	public void setDataLine(LinkedList<LineBean> dataLine) {
		this.dataLine = dataLine;
	}

	public HadoopFile getHadoopFile() {
		return hadoopFile;
	}

	public void setHadoopFile(HadoopFile hadoopFile) {
		this.hadoopFile = hadoopFile;
	}

	public LinkedList<AbstractAdaptor> getAdaptors() {
		return adaptors;
	}

	public void setAdaptors(LinkedList<AbstractAdaptor> adaptors) {
		this.adaptors = adaptors;
	}

	@Override
	public String toString() {
		return "HadoopTemplate [id=" + id + ", templateName=" + templateName + ", params=" + params + ", dataLine=" + dataLine + ", hadoopFile="
				+ hadoopFile.toString() + "]";
	}

	public void test(String data) throws Exception {
		try {
			logger.debug("Proccessing data : " + IOUtils.convertLogMessageToString(data));
			char startOfText = IOUtils.getAsciiByValue(String.valueOf(params.get("startChar")));
			char endOfText = IOUtils.getAsciiByValue(String.valueOf(params.get("endChar")));
			char delimeter = IOUtils.getAsciiByValue(String.valueOf(params.get("delimeterChar")));
			
			logger.debug("startOfText : " + (int)startOfText + " -- endOfText : " + (int)endOfText + " -- delimeter : " + (int)delimeter);

			int startIndex = data.indexOf(startOfText), endIndex = data.indexOf(endOfText);
			data = data.substring(startIndex + 2, endIndex);
			String[] columns = data.split(String.valueOf(delimeter));

			boolean valid = true;
			for (int i = 0; i < columns.length; i++) {
				String column = columns[i];
				LineBean bean = dataLine.get(i);
				logger.info(bean);
				
				valid = TemplateValidationProcessor.validdation(column, bean);
				if(!valid){
					break;
				}
				logger.info("Valid : " + valid);
			}
			
			if(valid){
				logger.info(" valid for template!!!!!!!!!!");
			}else{
				logger.info(" invalid for template!!!!!!!!!!");
			}

		} catch (Exception e) {
			logger.error(e, e);
		}
	}

}
