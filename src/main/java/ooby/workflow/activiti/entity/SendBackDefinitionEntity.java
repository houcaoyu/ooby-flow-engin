package ooby.workflow.activiti.entity;

import java.io.Serializable;

public class SendBackDefinitionEntity implements Serializable {
	private static final long serialVersionUID = 9126435182345335115L;
	private String activitiId;
	private boolean direct;

	public String getActivitiId() {
		return activitiId;
	}

	public void setActivitiId(String activitiId) {
		this.activitiId = activitiId;
	}

	public boolean isDirect() {
		return direct;
	}

	public void setDirect(boolean direct) {
		this.direct = direct;
	}

}
