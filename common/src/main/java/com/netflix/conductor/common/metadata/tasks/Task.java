/**
 * Copyright 2016 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */
package com.netflix.conductor.common.metadata.tasks;

import java.util.HashMap;
import java.util.Map;

import com.netflix.conductor.common.metadata.workflow.WorkflowTask;

/**
 * 
 * 
 *
 */
public class Task extends TaskResult {
	
	public enum Status {
		
		IN_PROGRESS(false, true, true), 
		CANCELED(true, false, false), 
		FAILED(true, false, true), 
		COMPLETED(true, true, true), 
		SCHEDULED(false, true, true), 
		TIMED_OUT(true, false, true),
		READY_FOR_RERUN(false, true, true),
		SKIPPED(true, true, false);
		
		private boolean terminal;
		
		private boolean successful;
		
		private boolean retriable;

		Status(boolean terminal, boolean successful, boolean retriable){
			this.terminal = terminal;
			this.successful = successful;
			this.retriable = retriable;
		}
		
		public boolean isTerminal(){
			return terminal;
		}
		
		public boolean isSuccessful(){
			return successful;
		}
		
		public boolean isRetriable(){
			return retriable;
		}
	};

	private String taskType;

	private Status status;

	private Map<String, Object> inputData = new HashMap<>();;

	private String referenceTaskName;

	private int retryCount;

	private int seq;

	private String correlationId;

	private int pollCount;
	
	private String taskDefName;

	/**
	 * Time when the task was scheduled
	 */
	private long scheduledTime;

	/**
	 * Time when the task was first polled
	 */
	private long startTime;

	/**
	 * Time when the task completed executing
	 */
	private long endTime;

	/**
	 * Time when the task was last updated
	 */
	private long updateTime;

	private int startDelayInSeconds;

	private String retriedTaskId;
	
	private boolean retried;
	
	private boolean callbackFromWorker = true;
	
	private WorkflowTask dynamicWorkflowTask;
	
	private int responseTimeoutSeconds;
	
	public Task(){
		
	}

	/**
	 * 
	 * @return Type of the task
	 * @see WorkflowTask.Type
	 */
	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
		if(TaskResult.isValidStatus(status.name())) {
			super.setStatus(TaskResult.Status.valueOf(status.name()));
		}
	}
	
	public Map<String, Object> getInputData() {
		return inputData;
	}

	public void setInputData(Map<String, Object> inputData) {
		this.inputData = inputData;
	}

	
	
	/**
	 * @return the referenceTaskName
	 */
	public String getReferenceTaskName() {
		return referenceTaskName;
	}

	/**
	 * @param referenceTaskName the referenceTaskName to set
	 */
	public void setReferenceTaskName(String referenceTaskName) {
		this.referenceTaskName = referenceTaskName;
	}

	/**
	 * @return the correlationId
	 */
	public String getCorrelationId() {
		return correlationId;
	}

	/**
	 * @param correlationId the correlationId to set
	 */
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	/**
	 * @return the retryCount
	 */
	public int getRetryCount() {
		return retryCount;
	}

	/**
	 * @param retryCount the retryCount to set
	 */
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	/**
	 * @return the scheduledTime
	 */
	public long getScheduledTime() {
		return scheduledTime;
	}

	/**
	 * @param scheduledTime the scheduledTime to set
	 */
	public void setScheduledTime(long scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	
	/**
	 * @return the startDelayInSeconds
	 */
	public int getStartDelayInSeconds() {
		return startDelayInSeconds;
	}

	/**
	 * @param startDelayInSeconds the startDelayInSeconds to set
	 */
	public void setStartDelayInSeconds(int startDelayInSeconds) {
		this.startDelayInSeconds = startDelayInSeconds;
	}
	
	/**
	 * @return the retriedTaskId
	 */
	public String getRetriedTaskId() {
		return retriedTaskId;
	}

	/**
	 * @param retriedTaskId the retriedTaskId to set
	 */
	public void setRetriedTaskId(String retriedTaskId) {
		this.retriedTaskId = retriedTaskId;
	}

	/**
	 * @return the seq
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}

	/**
	 * @return the updateTime
	 */
	public long getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	
	/**
	 * @return the queueWaitTime
	 */
	public long getQueueWaitTime() {
		if(this.startTime > 0 && this.scheduledTime > 0){
			return this.startTime - scheduledTime - (getCallbackAfterSeconds()*1000);
		}
		return 0L;
	}
	
	public void setQueueWaitTime(long t) {
		
	}
	
	
	/**
	 * 
	 * @return True if the task has been retried after failure
	 */
	public boolean isRetried() {
		return retried;
	}

	/**
	 * @param retried the retried to set
	 */
	public void setRetried(boolean retried) {
		this.retried = retried;
	}
	
	/**
	 * 
	 * @return No. of times task has been polled
	 */
	public int getPollCount() {
		return pollCount;
	}
	
	public void setPollCount(int pollCount) {
		this.pollCount = pollCount;
	}

	
	public boolean isCallbackFromWorker() {
		return callbackFromWorker;
	}

	public void setCallbackFromWorker(boolean callbackFromWorker) {
		this.callbackFromWorker = callbackFromWorker;
	}
	
	/**
	 * 
	 * @return Name of the task definition
	 */
	public String getTaskDefName() {
		//TODO: Is there a way to verify if there are no tasks with missing task def name?
		if(taskDefName == null || "".equals(taskDefName)){
			taskDefName = taskType;
		}
		return taskDefName;
	}
	public void setTaskDefName(String taskDefName) {
		this.taskDefName = taskDefName;
	}
	
	public WorkflowTask getDynamicWorkflowTask() {
		return dynamicWorkflowTask;
	}
	
	public void setDynamicWorkflowTask(WorkflowTask dynamicWorkflowTask) {
		this.dynamicWorkflowTask = dynamicWorkflowTask;
	}
	
	/**
	 * 
	 * @return the timeout for task to send response.  After this timeout, the task will be re-queued
	 */
	public int getResponseTimeoutSeconds() {
		return responseTimeoutSeconds;
	}
	
	/**
	 * 
	 * @param responseTimeoutSeconds - timeout for task to send response.  After this timeout, the task will be re-queued
	 */
	public void setResponseTimeoutSeconds(int responseTimeoutSeconds) {
		this.responseTimeoutSeconds = responseTimeoutSeconds;
	}

	
	public Task copy() {
		
		Task copy = new Task();
		copy.setCallbackAfterSeconds(getCallbackAfterSeconds());
		copy.setCallbackFromWorker(callbackFromWorker);
		copy.setCorrelationId(correlationId);
		copy.setDynamicWorkflowTask(dynamicWorkflowTask);
		copy.setInputData(inputData);
		copy.setOutputData(getOutputData());
		copy.setReferenceTaskName(referenceTaskName);
		copy.setStartDelayInSeconds(startDelayInSeconds);
		copy.setTaskDefName(taskDefName);
		copy.setTaskType(taskType);
		copy.setWorkflowInstanceId(getWorkflowInstanceId());
		copy.setResponseTimeoutSeconds(responseTimeoutSeconds);
		
		return copy;
	}
	

	@Override
	public String toString() {
		return "type="+ taskType + ", name=" + taskDefName + ", refName=" + referenceTaskName + ", taskId=" + getTaskId() + ", retry=" + retryCount + ", status=" + status;
	}
}
