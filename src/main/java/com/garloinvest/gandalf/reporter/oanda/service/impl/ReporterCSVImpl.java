package com.garloinvest.gandalf.reporter.oanda.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.garloinvest.gandalf.reporter.oanda.service.ReporterCSV;
import com.garloinvest.gandalf.util.DateUtil;

@Service
public class ReporterCSVImpl implements ReporterCSV {
	private static final Logger LOG = LoggerFactory.getLogger(ReporterCSVImpl.class);

	private static final String DELIMITER = ",";
	private static final String NEW_LINE = "\n";
	private static final String HEADER = "ID,LocalTime,PrevTime,PrevOpen,PrevClose,PrevSize,LastTime,LastOpen,LastClose,LastSize";
	private static final String CVSLINE = "csvLines";
	private static final String DATEFILE = "dateFile";
	private static final String CONFIG_CSV_FILE = "configCSV.properties";
	private Properties properties;
	private int id = 0;

	@Override
	public void savedCandleStickBUYSignal(String prevTime, BigDecimal prevOpen, BigDecimal prevClose,
			String lastTime, BigDecimal lastOpen, BigDecimal lastClose) {

		String localTime = LocalDateTime.now().toString();
		int pOpen = prevOpen.multiply(BigDecimal.valueOf(100000)).intValue();
		int pClose = prevClose.multiply(BigDecimal.valueOf(100000)).intValue();
		int prevSize = pClose - pOpen;
		int lOpen = lastOpen.multiply(BigDecimal.valueOf(100000)).intValue();
		int lClose = lastClose.multiply(BigDecimal.valueOf(100000)).intValue();
		int lastSize = lClose - lOpen;
		id++;

		int lines = readLines();
		if (lines == 0) {
			increaseLines(lines);
			savedDateFile(localTime);
			appendToNewFile(id, localTime, prevTime, prevOpen, prevClose, prevSize, lastTime, lastOpen, lastClose,
					lastSize);
		} else if (lines > 100) {
			resetLines();
			savedDateFile(localTime);
			appendToNewFile(id, localTime, prevTime, prevOpen, prevClose, prevSize, lastTime, lastOpen, lastClose,
					lastSize);
		}else {
			increaseLines(lines);
			String currentDateFile = readDateFile();
			appendToExistingFile(id, currentDateFile, prevTime, prevOpen, prevClose, prevSize, lastTime, lastOpen,
				lastClose, lastSize);
		}
		

	}

	private void appendToNewFile(int idF, String localTime, String prevTime, BigDecimal prevOpen, BigDecimal prevClose,
			int prevSize, String lastTime, BigDecimal lastOpen, BigDecimal lastClose, int lastSize) {
		PrintWriter printWriter = null;
		try {
			String time = DateUtil.formatDateToCsvFile(localTime);
			printWriter = new PrintWriter(new FileWriter("Trade" + time + ".csv"));
			StringBuilder sb = new StringBuilder();
			sb.append(HEADER);
			sb.append(NEW_LINE);
			sb.append(String.valueOf(idF)); // ID
			sb.append(DELIMITER);
			sb.append(localTime); // EST Time
			sb.append(DELIMITER);
			sb.append(prevTime); // PrevTime UTC
			sb.append(DELIMITER);
			sb.append(String.valueOf(prevOpen));// prevOpen
			sb.append(DELIMITER);
			sb.append(String.valueOf(prevClose));// prevClose
			sb.append(DELIMITER);
			sb.append(String.valueOf(prevSize));// prevSize
			sb.append(DELIMITER);
			sb.append(lastTime); // lastTime UTC
			sb.append(DELIMITER);
			sb.append(String.valueOf(lastOpen));// lastOpen
			sb.append(DELIMITER);
			sb.append(String.valueOf(lastClose));// lastClose
			sb.append(DELIMITER);
			sb.append(String.valueOf(lastSize));// lastSize

			printWriter.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("Error parsing a new Trade file: {}", e.getMessage());
		} finally {
			printWriter.close();
		}
	}

	private void appendToExistingFile(int idF, String currentDateFile, String prevTime, BigDecimal prevOpen,
			BigDecimal prevClose, int prevSize, String lastTime, BigDecimal lastOpen, BigDecimal lastClose,
			int lastSize) {
		String time = DateUtil.formatDateToCsvFile(currentDateFile);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("Trade" + time + ".csv", true);
			fileWriter.append(NEW_LINE);
			fileWriter.append(String.valueOf(idF)); // ID
			fileWriter.append(DELIMITER);
			fileWriter.append(currentDateFile); // EST Time
			fileWriter.append(DELIMITER);
			fileWriter.append(prevTime); // PrevTime UTC
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(prevOpen));// prevOpen
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(prevClose));// prevClose
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(prevSize));// prevSize
			fileWriter.append(DELIMITER);
			fileWriter.append(lastTime); // lastTime UTC
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(lastOpen));// lastOpen
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(lastClose));// lastClose
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(lastSize));// lastSize

		} catch (IOException e) {
			LOG.error("Error parsing to existing Trade file: {}", e.getMessage());
			e.printStackTrace();
		} finally {
			if (null != fileWriter) {
				try {
					fileWriter.flush();
					fileWriter.close();
				} catch (Exception e2) {
					LOG.error("Error closing CSV file file: {}", e2.getMessage());
					e2.printStackTrace();
				}
			}
		}

	}

	/*private void resetLines() {
		properties = new Properties();
		OutputStream out = null;

		try {
			out = new FileOutputStream("configCSV.properties");
			properties.setProperty(CVSLINE, "0");
			properties.store(out, null);
		} catch (FileNotFoundException fe) {
			LOG.error("Error loading to write configCSV file: {}", fe.getMessage());
			fe.printStackTrace();
		} catch (IOException ie) {
			LOG.error("Error loading configCSV file: {}", ie.getMessage());
			ie.printStackTrace();
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					LOG.error("Error closing configCSV file: {}", e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}*/
	
	private void resetLines() {
		PropertiesConfiguration props = null;
		try {
			props = new PropertiesConfiguration(CONFIG_CSV_FILE);
			props.setProperty(CVSLINE, "0");
			props.save();
		} catch (ConfigurationException ce) {
			LOG.error("Error loading to write configCSV file: {}", ce.getMessage());
			ce.printStackTrace();
		}
	}

	private int readLines() {
		properties = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(CONFIG_CSV_FILE);
			properties.load(in);
			return Integer.parseInt(properties.getProperty(CVSLINE));
		} catch (FileNotFoundException fe) {
			LOG.error("Error reading configCSV file: {}", fe.getMessage());
			fe.printStackTrace();
		} catch (IOException ie) {
			LOG.error("Error loading configCSV file: {}", ie.getMessage());
			ie.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					LOG.error("Error closing configCSV file: {}", e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return 0;
	}

	/*private void savedDateFile(String localTime) {
		properties = new Properties();
		OutputStream out = null;

		try {
			out = new FileOutputStream("configCSV.properties");
			properties.setProperty(DATEFILE, localTime);
			properties.store(out, null);
		} catch (FileNotFoundException fe) {
			LOG.error("Error loading to write configCSV file: {}", fe.getMessage());
			fe.printStackTrace();
		} catch (IOException ie) {
			LOG.error("Error loading configCSV file: {}", ie.getMessage());
			ie.printStackTrace();
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					LOG.error("Error closing configCSV file: {}", e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}*/
	
	private void savedDateFile(String localTime) {
		PropertiesConfiguration props = null;
		try {
			props = new PropertiesConfiguration(CONFIG_CSV_FILE);
			props.setProperty(DATEFILE, localTime);
			props.save();
		} catch (ConfigurationException ce) {
			LOG.error("Error loading to write configCSV file: {}", ce.getMessage());
			ce.printStackTrace();
		}
	}

	private String readDateFile() {
		properties = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(CONFIG_CSV_FILE);
			properties.load(in);
			return properties.getProperty(DATEFILE);
		} catch (FileNotFoundException fe) {
			LOG.error("Error reading configCSV file: {}", fe.getMessage());
			fe.printStackTrace();
		} catch (IOException ie) {
			LOG.error("Error loading configCSV file: {}", ie.getMessage());
			ie.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					LOG.error("Error closing configCSV file: {}", e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/*private void increaseLines(int lines) {
		properties = new Properties();
		OutputStream out = null;
		lines++;

		try {
			out = new FileOutputStream("configCSV.properties");
			properties.setProperty(CVSLINE, String.valueOf(lines));
			properties.store(out, null);
		} catch (FileNotFoundException fe) {
			LOG.error("Error loading to write configCSV file: {}", fe.getMessage());
			fe.printStackTrace();
		} catch (IOException ie) {
			LOG.error("Error loading configCSV file: {}", ie.getMessage());
			ie.printStackTrace();
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					LOG.error("Error closing configCSV file: {}", e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}*/
	private void increaseLines(int lines) {
		PropertiesConfiguration props = null;
		lines++;
		try {
			props = new PropertiesConfiguration(CONFIG_CSV_FILE);
			props.setProperty(CVSLINE, String.valueOf(lines));
			props.save();
		} catch (ConfigurationException ce) {
			LOG.error("Error loading to write configCSV file: {}", ce.getMessage());
			ce.printStackTrace();
		}		
	}

}
