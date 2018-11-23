package com.garloinvest.gandalf.indicator.oanda.service;

import java.util.List;

import com.oanda.v20.position.Position;

public interface FXPosition {

	public List<Position> getAllPositions();
}
