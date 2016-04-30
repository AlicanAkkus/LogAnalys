package com.wora.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.log4j.Logger;

import com.wora.bean.LineBean;
import com.wora.constant.IOConstant;
import com.wora.constant.LogAnalysConstant;
import com.wora.db.H2DaoImpl;
import com.wora.enums.LineEnum;
import com.wora.util.ParseLineBean;

public class LogReduce extends Reducer<Text, IntWritable, Text, IntWritable> implements IOConstant {

//	Logger logger = Logger.getLogger(LogReduce.class);
//	private IntWritable result = new IntWritable(1);
//
//	Text maxMethod, minMethod, avgMethod, maxVariable, minVariable, avgVariable;
//	Text maxContextInitialize, minContextInitialize, avgContextInitialize;
//	Text maxContextDestroy, minContextDestroy, avgContextDestroy;
//
//	ArrayList lineBeans = new ArrayList<>();
//	ArrayList methodslineBeans = new ArrayList<>();
//	ArrayList variablelineBeans = new ArrayList<>();
//	ArrayList initializelineBeans = new ArrayList<>();
//	ArrayList destroylineBeans = new ArrayList<>();
//
//	MultipleOutputs<Text, Text> output;
//
//	int countMethod, countVariable, countDestroy, countInitialize;
//
//	@Override
//	public void setup(Context context) {
//		output = new MultipleOutputs(context);
//	}
//
//	@Override
//	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
//		// gelen text line'i dosyaya yazalım.
//		try {
//
//			String fileKey = "GENERAL";
//			System.out.println("Gelen text : " + key.toString());
//			logger.debug("Received line data : " + key.toString());
//			if (key.toString().indexOf(METHOD_DECLARATION) > -1) {
//				countMethod++;
//				fileKey = LogAnalysConstant.METHOD_OUTPUT_NAME;
//				LineBean lineBean = ParseLineBean.parse(key, LineEnum.METHOD_DECLARATION);
//				if (lineBean != null) {
//					H2DaoImpl dbService = (H2DaoImpl) Analys.getInstance().getDbService();
//					dbService.insertMethodLog((MethodLineBean) lineBean);
//					logger.debug(((MethodLineBean) lineBean).toString());
//					methodslineBeans.add(lineBean);
//					context.write(new Text(lineBean.toString()), result);
//				} else {
//					logger.error("Somethings went wrong while Line Parsing!");
//					System.out.println("Somethings went wrong while Line Parsing!");
//				}
//			}
//
//			if (key.toString().indexOf(VARIABLE_DECLARATION) > -1) {
//				countVariable++;
//				fileKey = LogAnalysConstant.VARIABLE_OUTPUT_NAME;
//				LineBean lineBean = ParseLineBean.parse(key, LineEnum.VARIABLE_DECLARATION);
//				if (lineBean != null) {
//					H2DaoImpl dbService = (H2DaoImpl) Analys.getInstance().getDbService();
//					dbService.insertVariableMethodLog((VariableLineBean) lineBean);
//					logger.debug(((VariableLineBean) lineBean).toString());
//					variablelineBeans.add(lineBean);
//					context.write(new Text(lineBean.toString()), result);
//				} else {
//					logger.error("Somethings went wrong while Line Parsing!");
//					System.out.println("Somethings went wrong while Line Parsing!");
//				}
//			}
//
//			if (key.toString().indexOf(INITALIZE_DECLARATION) > -1) {
//				countInitialize++;
//				fileKey = LogAnalysConstant.INITALIZE_OUTPUT_NAME;
//				LineBean lineBean = ParseLineBean.parse(key, LineEnum.INITALIZE_DECLARATION);
//				if (lineBean != null) {
//					H2DaoImpl dbService = (H2DaoImpl) Analys.getInstance().getDbService();
//					dbService.insertContextInitializeLog((ContextInitializeLineBean) lineBean);
//					logger.debug(((ContextInitializeLineBean) lineBean).toString());
//					initializelineBeans.add(lineBean);
//					context.write(new Text(lineBean.toString()), result);
//				} else {
//					logger.error("Somethings went wrong while Line Parsing!");
//					System.out.println("Somethings went wrong while Line Parsing!");
//				}
//
//			}
//
//			if (key.toString().indexOf(DESTROY_DECLARATION) > -1) {
//				countDestroy++;
//				fileKey = LogAnalysConstant.DESTROY_OUTPUT_NAME;
//				LineBean lineBean = ParseLineBean.parse(key, LineEnum.DESTROY_DECLARATION);
//				if (lineBean != null) {
//					H2DaoImpl dbService = (H2DaoImpl) Analys.getInstance().getDbService();
//					dbService.insertContextDestroyLog((ContextDestroyLineBean) lineBean);
//					logger.debug(((ContextDestroyLineBean) lineBean).toString());
//					destroylineBeans.add(lineBean);
//					context.write(new Text(lineBean.toString()), result);
//				} else {
//					logger.error("Somethings went wrong while Line Parsing!");
//					System.out.println("Somethings went wrong while Line Parsing!");
//				}
//			}
//
//			// System.out.println("Dosya \'" + fileKey + "\' hedefine yazdırıldı");
//			// logger.debug("Dosya \'" + fileKey + "\' hedefine yazdırıldı");
//			// output.write(fileKey, NullWritable.get(), key, fileKey);
//
//			// context.write(key, result);
//
//		} catch (Exception e) {
//			logger.error(e, e);
//		}
//
//	}
//
//	@Override
//	protected void cleanup(Context context) throws IOException, InterruptedException {
//		output.close();
//		logger.debug("Cleanup context..");
//		System.out.println("Cleanup context..");
//
//		Collections.sort(methodslineBeans);
//		Collections.sort(variablelineBeans);
//		Collections.sort(initializelineBeans);
//		Collections.sort(destroylineBeans);
//
//		if (!methodslineBeans.isEmpty()) {
//			int min = ((MethodLineBean) methodslineBeans.get(0)).getExecuteTime();
//			int max = ((MethodLineBean) methodslineBeans.get(methodslineBeans.size() - 1)).getExecuteTime();
//			int avg = (max + min) / methodslineBeans.size();
//
//			maxMethod = new Text("Max Executed Method");
//			minMethod = new Text("Min Executed Method");
//			avgMethod = new Text("Avg Executed Method");
//
//			context.write(maxMethod, new IntWritable(max));
//			context.write(minMethod, new IntWritable(min));
//			context.write(avgMethod, new IntWritable(avg));
//
//			MethodLineBean maxMethodLine = (MethodLineBean) methodslineBeans.get(0);
//			maxMethodLine.setAnalysType("MAX");
//			logger.debug("Max Executed Method : " + maxMethodLine.toString());
//
//			MethodLineBean minMethodLine = (MethodLineBean) methodslineBeans.get(methodslineBeans.size() - 1);
//			minMethodLine.setAnalysType("Min");
//			logger.debug("Min Executed Method : " + minMethodLine.toString());
//
//			MethodLineBean avgMethodLine = new MethodLineBean(LineEnum.METHOD_DECLARATION, "AVG METHOD", avg, new Date());
//			logger.debug("Avg Executed Method : " + avgMethodLine.toString());
//
//			H2DaoImpl dbService = (H2DaoImpl) Analys.getInstance().getDbService();
//			dbService.insertMethodLog(maxMethodLine);
//			dbService.insertMethodLog(minMethodLine);
//			dbService.insertMethodLog(avgMethodLine);
//
//		} else {
//			logger.debug("Methods Line bean is empty!");
//			System.out.println("Methods Line bean is empty!");
//		}
//
//		if (!variablelineBeans.isEmpty()) {
//			int min = ((VariableLineBean) variablelineBeans.get(0)).getSumMemory();
//			int max = ((VariableLineBean) variablelineBeans.get(variablelineBeans.size() - 1)).getSumMemory();
//			int avg = (max + min) / variablelineBeans.size();
//
//			maxVariable = new Text("Max Memory Usage");
//			minVariable = new Text("Min Memory Usage");
//			avgVariable = new Text("Avg Memory Usage");
//
//			context.write(maxVariable, new IntWritable(max));
//			context.write(minVariable, new IntWritable(min));
//			context.write(avgVariable, new IntWritable(avg));
//
//			VariableLineBean maxVariableLine = (VariableLineBean) variablelineBeans.get(0);
//			maxVariableLine.setAnalysType("MAX");
//			logger.debug("Max Variable Memory : " + maxVariableLine.toString());
//
//			VariableLineBean minVariableLine = (VariableLineBean) variablelineBeans.get(variablelineBeans.size() - 1);
//			minVariableLine.setAnalysType("Min");
//			logger.debug("Max Variable Memory : " + minVariableLine.toString());
//
//			VariableLineBean avgVariableLine = new VariableLineBean(LineEnum.VARIABLE_DECLARATION, "AVG VARIABLE", avg, new Date());
//			logger.debug("Max Variable Memory : " + avgVariableLine.toString());
//
//			H2DaoImpl dbService = (H2DaoImpl) Analys.getInstance().getDbService();
//			dbService.insertVariableMethodLog(maxVariableLine);
//			dbService.insertVariableMethodLog(minVariableLine);
//			dbService.insertVariableMethodLog(avgVariableLine);
//		} else {
//			logger.debug("Variable Line bean is empty!");
//			System.out.println("Variable Line bean is empty!");
//		}
//
//		if (!initializelineBeans.isEmpty()) {
//			int min = ((ContextInitializeLineBean) initializelineBeans.get(0)).getExecuteTime();
//			int max = ((ContextInitializeLineBean) initializelineBeans.get(initializelineBeans.size() - 1)).getExecuteTime();
//			int avg = (max + min) / initializelineBeans.size();
//
//			maxContextInitialize = new Text("Max Context Initializing");
//			minContextInitialize = new Text("Min Context Initializing");
//			avgContextInitialize = new Text("Avg Context Initializing");
//
//			context.write(maxContextInitialize, new IntWritable(max));
//			context.write(maxContextInitialize, new IntWritable(min));
//			context.write(maxContextInitialize, new IntWritable(avg));
//
//			ContextInitializeLineBean maxContextInitLine = (ContextInitializeLineBean) initializelineBeans.get(0);
//			maxContextInitLine.setAnalysType("MAX");
//			logger.debug("Max Context Initialize : " + maxContextInitLine.toString());
//
//			ContextInitializeLineBean minContextInitLine = (ContextInitializeLineBean) initializelineBeans.get(initializelineBeans.size() - 1);
//			minContextInitLine.setAnalysType("Min");
//			logger.debug("Max Context Initialize : " + minContextInitLine.toString());
//
//			ContextInitializeLineBean avgContextInitLine = new ContextInitializeLineBean(LineEnum.INITALIZE_DECLARATION, "AVG CONTEXT INITIALIZE",
//					"AVG CONTEXT INITIALIZE", avg, new Date());
//			logger.debug("Max Context Initialize : " + avgContextInitLine.toString());
//
//			H2DaoImpl dbService = (H2DaoImpl) Analys.getInstance().getDbService();
//			dbService.insertContextInitializeLog(maxContextInitLine);
//			dbService.insertContextInitializeLog(minContextInitLine);
//			dbService.insertContextInitializeLog(avgContextInitLine);
//		} else {
//			logger.debug("Initialize Line bean is empty!");
//			System.out.println("Initialize Line bean is empty!");
//		}
//
//		if (!destroylineBeans.isEmpty()) {
//			int min = ((ContextDestroyLineBean) destroylineBeans.get(0)).getExecuteTime();
//			int max = ((MethodLineBean) destroylineBeans.get(destroylineBeans.size() - 1)).getExecuteTime();
//			int avg = (max + min) / destroylineBeans.size();
//
//			maxContextDestroy = new Text("Max Context Destroyed");
//			minContextDestroy = new Text("Min Context Destroyed");
//			avgContextDestroy = new Text("Avg Context Destroyed");
//
//			context.write(maxContextDestroy, new IntWritable(max));
//			context.write(minContextDestroy, new IntWritable(min));
//			context.write(avgContextDestroy, new IntWritable(avg));
//
//			ContextDestroyLineBean maxContextInitLine = (ContextDestroyLineBean) destroylineBeans.get(0);
//			maxContextInitLine.setAnalysType("MAX");
//			logger.debug("Max Context DESTROY : " + maxContextInitLine.toString());
//
//			ContextDestroyLineBean minContextInitLine = (ContextDestroyLineBean) destroylineBeans.get(destroylineBeans.size() - 1);
//			minContextInitLine.setAnalysType("Min");
//			logger.debug("Max Context DESTROY : " + minContextInitLine.toString());
//
//			ContextDestroyLineBean avgContextInitLine = new ContextDestroyLineBean(LineEnum.INITALIZE_DECLARATION, "AVG CONTEXT DESTROY",
//					"AVG CONTEXT DESTROY", avg, new Date());
//			logger.debug("Max Context DESTROY : " + avgContextInitLine.toString());
//
//			H2DaoImpl dbService = (H2DaoImpl) Analys.getInstance().getDbService();
//			dbService.insertContextDestroyLog(maxContextInitLine);
//			dbService.insertContextDestroyLog(minContextInitLine);
//			dbService.insertContextDestroyLog(avgContextInitLine);
//		} else {
//			logger.debug("Destroyed Line bean is empty!");
//			System.out.println("Destroyed Line bean is empty!");
//		}
//
//		StringBuilder log = new StringBuilder();
//		log.append("Method Count : ").append(countMethod).append("\n");
//		log.append("Variable Count : ").append(countVariable).append("\n");
//		log.append("Initializing Count : ").append(countInitialize).append("\n");
//		log.append("Destroyed Count : ").append(countDestroy).append("\n");
//		logger.debug(log);
//		System.out.println(log.toString());
//
//		context.write(new Text("Method Count"), new IntWritable(countMethod));
//		context.write(new Text("Variable Count"), new IntWritable(countVariable));
//		context.write(new Text("Initializing Count"), new IntWritable(countInitialize));
//		context.write(new Text("Destroyed Count"), new IntWritable(countDestroy));
//
//	}
//
//	String generateFileName(Text key) {
//		System.out.println("Dosya adi : " + key.toString() + "" + System.currentTimeMillis());
//		logger.debug("Dosya adi : " + key.toString() + "" + System.currentTimeMillis());
//		return key.toString() + "" + System.currentTimeMillis();
//	}

}

class MultiFileOutput extends MultipleTextOutputFormat<Text, Text> {
	@Override
	protected String generateFileNameForKeyValue(Text key, Text value, String name) {
		return key.toString();
	}
}