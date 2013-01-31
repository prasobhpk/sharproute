package com.sharproute.common.object;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class AbstractCommonObject implements Serializable {
	
	public String getType() {
		return this.getClass().getSimpleName();
	}

}
