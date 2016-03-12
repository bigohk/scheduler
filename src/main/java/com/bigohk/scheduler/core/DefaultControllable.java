package com.bigohk.scheduler.core;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.bigohk.scheduler.api.Function;
import com.bigohk.scheduler.api.FunctionCompletionCallback;
import com.bigohk.scheduler.api.FunctionExceptionCallback;
import com.bigohk.scheduler.api.StateChangeCallback;

public class DefaultControllable implements Controllable {
	private final AtomicBoolean								shouldPause				= new AtomicBoolean(false);
	private final AtomicLong								latency					= new AtomicLong();
	private final HashMap<TaskState, StateChangeCallback>	stateChangeCallbacks	= new HashMap<>(
																							TaskState.values().length);

	private final SchedulingId								schedulingId;
	private Function										function;
	private TaskState										taskState;
	private FunctionCompletionCallback						functionCompletionCallback;
	private boolean											fnCompCallbackOnThread;
	private FunctionExceptionCallback						functionExceptionCallback;
	private boolean											fnExcpCallbackOnThread;

	public DefaultControllable(SchedulingId id, Function function) {
		this.schedulingId = id;
		this.function = function;
	}

	@Override
	public SchedulingId getSchedulingId() {
		return schedulingId;
	}

	@Override
	public void pause() {
		shouldPause.set(true);
		taskState = TaskState.PAUSING;
		issueStateChangeNotification();
	}

	@Override
	public void resume() {
		shouldPause.set(false);
		taskState = TaskState.RUNNABLE;
		issueStateChangeNotification();
	}

	@Override
	public TaskState getTaskState() {
		return taskState;
	}

	@Override
	public void attachStateChangeCallback(TaskState state, StateChangeCallback callback) {
		stateChangeCallbacks.put(state, callback);
	}

	@Override
	public void attachFunction(Function function) { // Hack -- maybe remove
		this.function = function;
	}

	@Override
	public void attachFunctionCompletionCallback(FunctionCompletionCallback callback, boolean useSeparateThread) {
		this.functionCompletionCallback = callback;
		fnCompCallbackOnThread = useSeparateThread;
	}

	@Override
	public void attachFunctionExceptionCallback(FunctionExceptionCallback callback, boolean useSeparateThread) {
		this.functionExceptionCallback = callback;
		fnExcpCallbackOnThread = useSeparateThread;
	}

	@Override
	public long getLastFunctionLatency() {
		return latency.get();
	}

	@Override
	public void run() {
		// System.out.println(shouldPause + " " + taskState);

		if (shouldPause.get()) {
			if (taskState == TaskState.PAUSING) {
				taskState = TaskState.PAUSED;
				issueStateChangeNotification();
				return;
			} else if (taskState == TaskState.PAUSED) {
				return;
			}
		}

		if (taskState != TaskState.RUNNING) {
			taskState = TaskState.RUNNING;
			issueStateChangeNotification();
		}

		long t0 = System.nanoTime();
		try {
			function.perform();
			issueFunctionCompletionCallback();
		} catch (Throwable throwable) { // Since we are handing over the control
										// outside of here !
			if (throwable instanceof Exception) {
				issueFunctionExceptionCallback();
			}
		} finally {
			long t1 = System.nanoTime();
			latency.set(t1 - t0);
		}
	}

	private void issueFunctionCompletionCallback() {
		if (functionCompletionCallback != null) {
			if (fnCompCallbackOnThread) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						functionCompletionCallback.onFunctionComplete();
					}
				}).start();
			} else {
				functionCompletionCallback.onFunctionComplete();
			}
		}
	}

	private void issueFunctionExceptionCallback() {
		if (functionExceptionCallback != null) {
			if (fnExcpCallbackOnThread) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						functionExceptionCallback.onFunctionException();
					}
				}).start();
			}
		}
	}

	private void issueStateChangeNotification() {
		final StateChangeCallback stateChangeCallback = stateChangeCallbacks.get(taskState);
		if (stateChangeCallback != null) {
			stateChangeCallback.notifyStateChange(taskState);
		}
	}
}
