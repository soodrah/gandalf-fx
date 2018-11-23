package com.garloinvest.gandalf.indicator.oanda.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.garloinvest.gandalf.constants.GlobalConstants;
import com.garloinvest.gandalf.indicator.oanda.service.FXPosition;
import com.garloinvest.gandalf.searcher.oanda.connection.OandaConnectionFXPractice;
import com.oanda.v20.Context;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.position.Position;
import com.oanda.v20.position.PositionContext;

@Service
public class FXPositionImpl implements FXPosition {
	private static final Logger LOG = LoggerFactory.getLogger(FXPositionImpl.class);
	
	@Autowired
	private Environment environment;
	@Autowired
	private OandaConnectionFXPractice connection;
	
	@Override
	public List<Position> getAllPositions() {
		Context context = connection.getConnectionFXPractice();
		PositionContext positionCtx = new PositionContext(context);
		List<Position> positionList = new ArrayList<>();
		try {
			positionList =  positionCtx.list(new AccountID(environment.getProperty(GlobalConstants.ACCOUNTID_FX_PRACTICE))).getPositions();
		} catch (RequestException e) {
			LOG.error("Error Requesting Position List on FXPositionImpl: {}",e.getErrorMessage());
			e.printStackTrace();
		} catch (ExecuteException e) {
			LOG.error("Error Execution on FXPositionImpl: {}",e.getMessage());
			e.printStackTrace();
		}
		return positionList;
	}

}
