package com.wora.main;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import com.wora.constant.IOConstant;

public class LogMapper extends Mapper<LongWritable, Text, Text, IntWritable> implements IOConstant {

	Logger logger = Logger.getLogger(LogMapper.class);
	private final static IntWritable one = new IntWritable(1);

	private Text word = new Text();

	@Override
	public void map(LongWritable key, Text value, Mapper.Context context) throws IOException, InterruptedException {
		String data = value.toString();
		int beginOfText = data.indexOf(STX), endOfText = data.indexOf(ETX);

		// logger daki ilgilendigimiz kisimlari alalim sadece.
		if (beginOfText != -1 && endOfText != -1 && beginOfText != endOfText) {
			data = data.substring(beginOfText, endOfText + 1);

			logger.debug("Mapping set : " + data);
			System.out.println("Mapping set : " + data);
			word.set(data);
			context.write(word, one);
		} else {
			// TODO ilgilendigimiz kisimlar yoksa ne yapsin?
		}
	}
}