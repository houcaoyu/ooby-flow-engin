package ooby.workflow.activiti.entity;

import org.activiti.engine.impl.persistence.entity.GroupEntity;

public class OobyGroupEntity extends GroupEntity{

	private static final long serialVersionUID = 8830587875712146437L;
	
	private String parent;

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}


	
	
}
