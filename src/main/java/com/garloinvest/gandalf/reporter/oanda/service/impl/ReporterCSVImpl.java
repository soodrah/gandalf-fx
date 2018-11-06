package com.garloinvest.gandalf.reporter.oanda.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
	private static final String HEADER_CANDLE = "ID,LocalTime,PrevTime,PrevOpen,PrevClose,PrevSize,LastTime,LastOpen,LastClose,LastSize,CurrentTime,CurrentOpen,CurrentClose,CurrentSize,Rule";
	private static final String HEADER_PRICE = "ID,LocalTime,PriceTime,Tradeable,BUYPrice,BUYVolume,SELLPrice,SELLVolume,SPREAD";
	private static final String CSVLINEB = "csvLines";
	private static final String DATEFILEB = "dateFile";
	private static final String DATEFILEPRICE = "dateFilePrice";
	private static final String IDBUY = "idBuy";
	private static final String IDREJECT = "idRejected";
	private static final String IDPRICE = "idPrice";
	private static final String CSVLINER = "csvLineReject";
	private static final String CSVLINEPRICE = "csvLinePrice";
	private static final String DATEFILER = "dateFileReject";
	private static final String CONFIG_CSV_FILE = "configCSV.properties";
	private static final String RULE_1 = "Rule1";
	private static final String RULE_2 = "Rule2";
	private static final String RULE_3 = "Rule3";
	private Properties properties;

	@Override
	public void savedCandleStickBUYSignal(String prevTime, BigDecimal prevOpen, BigDecimal prevClose,
			String lastTime, BigDecimal lastOpen, BigDecimal lastClose,String currentTime, BigDecimal currentOpen, BigDecimal currentClose, String rule) {

		String localTime = LocalDateTime.now().toString();
		int pOpen = prevOpen.multiply(BigDecimal.valueOf(100000)).intValue();	 //prevOpen
		int pClose = prevClose.multiply(BigDecimal.valueOf(100000)).intValue();	 //prevClose
		int prevSize = pClose - pOpen;
		int lOpen = lastOpen.multiply(BigDecimal.valueOf(100000)).intValue();	 //lastOpen
		int lClose = lastClose.multiply(BigDecimal.valueOf(100000)).intValue();	 //lastClose
		int lastSize = lClose - lOpen;
		int cOpen = currentOpen.multiply(BigDecimal.valueOf(100000)).intValue();  //currentOpen	
		int cClose = currentClose.multiply(BigDecimal.valueOf(100000)).intValue();//currentClose
		int currentSize = cClose - cOpen;
		int id = readCount(IDBUY);
		increaseCount(IDBUY, id);
		
		String ruleNo = checkedRule(rule);
		int amount = readCount(ruleNo);
		increaseCount(ruleNo, amount);

		int lines = readCount(CSVLINEB);
		if (lines == 0) {
			increaseCount(CSVLINEB,lines);
			savedDateFile(DATEFILEB,localTime);
			appendToNewFile("TRADE-BUY",id, localTime, prevTime, prevOpen, prevClose, prevSize, 
					lastTime, lastOpen, lastClose,lastSize,
					currentTime,currentOpen,currentClose,currentSize, ruleNo);
		} else if (lines > 100) {
			resetLines(CSVLINEB);
			savedDateFile(DATEFILEB,localTime);
			appendToNewFile("TRADE-BUY",id, localTime, prevTime, prevOpen, prevClose, prevSize, 
					lastTime, lastOpen, lastClose, lastSize,
					currentTime,currentOpen,currentClose,currentSize, ruleNo);
		}else {
			increaseCount(CSVLINEB,lines);
			String currentDateFile = readDateFile(DATEFILEB);
			appendToExistingFile("TRADE-BUY",currentDateFile,id, localTime, prevTime, prevOpen, prevClose, prevSize, 
					lastTime, lastOpen, lastClose, lastSize, 
					currentTime,currentOpen,currentClose,currentSize, ruleNo);
		}
		

	}
	

	@Override
	public void storeRejectCandleData(String prevTime, BigDecimal prevOpen, BigDecimal prevClose, String lastTime,
			BigDecimal lastOpen, BigDecimal lastClose,String currentTime, BigDecimal currentOpen, BigDecimal currentClose, String rule) {
		
		String localTime = LocalDateTime.now().toString();
		int pOpen = prevOpen.multiply(BigDecimal.valueOf(100000)).intValue();
		int pClose = prevClose.multiply(BigDecimal.valueOf(100000)).intValue();
		int prevSize = pClose - pOpen;
		int lOpen = lastOpen.multiply(BigDecimal.valueOf(100000)).intValue();
		int lClose = lastClose.multiply(BigDecimal.valueOf(100000)).intValue();
		int lastSize = lClose - lOpen;
		int cOpen = currentOpen.multiply(BigDecimal.valueOf(100000)).intValue();
		int cClose = currentClose.multiply(BigDecimal.valueOf(100000)).intValue();
		int currentSize = cClose - cOpen;
		int id = readCount(IDREJECT);
		increaseCount(IDREJECT, id);
		
		String ruleNo = checkedRule(rule);

		int lines = readCount(CSVLINER);
		if (lines == 0) {
			increaseCount(CSVLINER,lines);
			savedDateFile(DATEFILER,localTime);
			appendToNewFile("TRADE-REJECT",id, localTime, prevTime, prevOpen, prevClose, prevSize, 
					lastTime, lastOpen, lastClose, lastSize, 
					currentTime, currentOpen, currentClose, currentSize, ruleNo);
		} else if (lines > 100) {
			resetLines(CSVLINER);
			savedDateFile(DATEFILER,localTime);
			appendToNewFile("TRADE-REJECT",id, localTime, prevTime, prevOpen, prevClose, prevSize, 
					lastTime, lastOpen, lastClose, lastSize, 
					currentTime, currentOpen, currentClose, currentSize, ruleNo);
		}else {
			increaseCount(CSVLINER,lines);
			String currentDateFile = readDateFile(DATEFILER);
			appendToExistingFile("TRADE-REJECT",currentDateFile,id, localTime, prevTime, prevOpen, prevClose, prevSize, 
					lastTime, lastOpen, lastClose, lastSize, 
					currentTime, currentOpen, currentClose, currentSize, ruleNo);
		}
		
	}
	
	@Override
	public void savedCurrentPrice(String instrument, LocalDateTime time, boolean tradeable, BigDecimal buy,
			Long liquidityBuy, BigDecimal sell, Long liquiditySell) {
		
		String localTime = LocalDateTime.now().toString();
		int buyP = buy.multiply(BigDecimal.valueOf(100000)).intValue();
		int sellP = sell.multiply(BigDecimal.valueOf(100000)).intValue();
		int spread = buyP - sellP;
		
		int id = readCount(IDPRICE);
		increaseCount(IDPRICE, id);
		int lines = readCount(CSVLINEPRICE);
		
		if (lines == 0) {
			increaseCount(CSVLINEPRICE,lines);
			savedDateFile(DATEFILEPRICE,localTime);
			appendToNewFilePrice("TRADE-PRICE-"+instrument,id, localTime, time, tradeable, buy, liquidityBuy, sell, liquiditySell, spread);
		} else if (lines > 100) {
			resetLines(CSVLINEPRICE);
			savedDateFile(DATEFILEPRICE,localTime);
			appendToNewFilePrice("TRADE-PRICE-"+instrument,id, localTime, time, tradeable, buy, liquidityBuy, sell, liquiditySell, spread);
		}else {
			increaseCount(CSVLINEPRICE,lines);
			String currentDateFile = readDateFile(DATEFILEPRICE);
			appendToExistingFilePrice("TRADE-PRICE-"+instrument,currentDateFile,id, localTime, time, tradeable, buy, liquidityBuy, sell, liquiditySell, spread);
		}
		
	}

	private void appendToNewFilePrice(String fileName, int id, String localTime, LocalDateTime time, boolean tradeable,
			BigDecimal buy, Long liquidityBuy, BigDecimal sell, Long liquiditySell, int spread) {
		
		PrintWriter printWriter = null;
		try {
			String timeTab = DateUtil.formatDateToCsvFile(localTime);
			printWriter = new PrintWriter(new FileWriter(fileName + timeTab + ".csv"));
			StringBuilder sb = new StringBuilder();
			sb.append(HEADER_PRICE);
			sb.append(NEW_LINE);
			sb.append(String.valueOf(id)); 				// ID
			sb.append(DELIMITER);
			sb.append(localTime); 						// EST Time
			sb.append(DELIMITER);
			sb.append(String.valueOf(time));			// Time UTC
			sb.append(DELIMITER);
			sb.append(String.valueOf(tradeable));		// Tradeable
			sb.append(DELIMITER);
			sb.append(String.valueOf(buy));				// Buy-Price(Ask)
			sb.append(DELIMITER);
			sb.append(String.valueOf(liquidityBuy));	// Liquidity
			sb.append(DELIMITER);
			sb.append(String.valueOf(sell));			// Sell-Price(Bid)
			sb.append(DELIMITER);
			sb.append(String.valueOf(liquiditySell));	// Liquidity
			sb.append(DELIMITER);
			sb.append(String.valueOf(spread));			// Spread

			printWriter.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("Error parsing a new Trade file: {}", e.getMessage());
		} finally {
			printWriter.close();
		}
		
	}


	private void appendToNewFile(String fileName, int idF, String localTime, String prevTime, BigDecimal prevOpen, BigDecimal prevClose, int prevSize, 
			String lastTime, BigDecimal lastOpen, BigDecimal lastClose, int lastSize, 
			String currentTime, BigDecimal currentOpen, BigDecimal currentClose, int currentSize, String ruleNo) {
		PrintWriter printWriter = null;
		try {
			String time = DateUtil.formatDateToCsvFile(localTime);
			printWriter = new PrintWriter(new FileWriter(fileName + time + ".csv"));
			StringBuilder sb = new StringBuilder();
			sb.append(HEADER_CANDLE);
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
			sb.append(DELIMITER);
			sb.append(currentTime); // currentTime UTC
			sb.append(DELIMITER);
			sb.append(String.valueOf(currentOpen));// currentOpen
			sb.append(DELIMITER);
			sb.append(String.valueOf(currentClose));// currentClose
			sb.append(DELIMITER);
			sb.append(String.valueOf(currentSize));// currentSize
			sb.append(DELIMITER);
			sb.append(String.valueOf(ruleNo));// RuleNo.

			printWriter.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("Error parsing a new Trade file: {}", e.getMessage());
		} finally {
			printWriter.close();
		}
	}

	private void appendToExistingFilePrice(String fileName, String currentDateFile, int id, String localTime,
			LocalDateTime time, boolean tradeable, BigDecimal buy, Long liquidityBuy, BigDecimal sell,
			Long liquiditySell, int spread) {
		
		String timeTab = DateUtil.formatDateToCsvFile(currentDateFile);
		FileWriter fileWriter = null;
		
		try {
			fileWriter = new FileWriter(fileName + timeTab + ".csv", true);
			fileWriter.append(NEW_LINE);
			fileWriter.append(String.valueOf(id)); 				// ID
			fileWriter.append(DELIMITER);
			fileWriter.append(localTime); 						// EST Time
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(time));			// Time UTC
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(tradeable));		// Tradeable
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(buy));				// Buy-Price(Ask)
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(liquidityBuy));	// Liquidity
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(sell));			// Sell-Price(Bid)
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(liquiditySell));	// Liquidity
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(spread));			// Spread

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
	
	private void appendToExistingFile(String fileName, String currentDateFile, int idF, String localTime, String prevTime, BigDecimal prevOpen,
			BigDecimal prevClose, int prevSize, 
			String lastTime, BigDecimal lastOpen, BigDecimal lastClose, int lastSize, 
			String currentTime, BigDecimal currentOpen, BigDecimal currentClose, int currentSize, String ruleNo) {
		
		String time = DateUtil.formatDateToCsvFile(currentDateFile);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(fileName + time + ".csv", true);
			fileWriter.append(NEW_LINE);
			fileWriter.append(String.valueOf(idF)); // ID
			fileWriter.append(DELIMITER);
			fileWriter.append(localTime); // EST Time
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
			fileWriter.append(DELIMITER);
			fileWriter.append(currentTime); // currentTime UTC
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(currentOpen));// currentOpen
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(currentClose));// currentClose
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(currentSize));// currentSize
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(ruleNo));// RuleNo

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
	
	private void resetLines(String key) {
		PropertiesConfiguration props = null;
		try {
			props = new PropertiesConfiguration(CONFIG_CSV_FILE);
			props.setProperty(key, "1");
			props.save();
		} catch (ConfigurationException ce) {
			LOG.error("Error loading to write configCSV file: {}", ce.getMessage());
			ce.printStackTrace();
		}
	}

	private int readCount(String key) {
		properties = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(CONFIG_CSV_FILE);
			properties.load(in);
			return Integer.parseInt(properties.getProperty(key));
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
	
	private void savedDateFile(String key, String localTime) {
		PropertiesConfiguration props = null;
		try {
			props = new PropertiesConfiguration(CONFIG_CSV_FILE);
			props.setProperty(key, localTime);
			props.save();
		} catch (ConfigurationException ce) {
			LOG.error("Error loading to write configCSV file: {}", ce.getMessage());
			ce.printStackTrace();
		}
	}

	private String readDateFile(String key) {
		properties = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(CONFIG_CSV_FILE);
			properties.load(in);
			return properties.getProperty(key);
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
	
	private void increaseCount(String key, int value) {
		PropertiesConfiguration props = null;
		value++;
		try {
			props = new PropertiesConfiguration(CONFIG_CSV_FILE);
			props.setProperty(key, String.valueOf(value));
			props.save();
		} catch (ConfigurationException ce) {
			LOG.error("Error loading to write configCSV file: {}", ce.getMessage());
			ce.printStackTrace();
		}		
	}
	
	private String checkedRule(String rule) {
		if(rule.equals(RULE_1)) {
			return RULE_1;
		}else if(rule.equals(RULE_2)) {
			return RULE_2;
		}else if(rule.equals(RULE_3)) {
			return RULE_3;
		}else {
			return "NONE";
		}
	}

}
