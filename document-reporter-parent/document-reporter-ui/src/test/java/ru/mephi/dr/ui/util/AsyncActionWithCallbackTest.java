package ru.mephi.dr.ui.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.mockito.Mockito;
import org.testng.annotations.Test;

import ru.mephi.dr.ui.util.AsyncActionWithCallback.Lambda;

public class AsyncActionWithCallbackTest {

	@Test
	public void submit() throws InterruptedException, ExecutionException {
		AsyncActionWithCallback sut = new AsyncActionWithCallback();
		Lambda action = new Lambda() {
			@Override
			public void action() {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		};
		action = Mockito.spy(action);
		Lambda callback = Mockito.spy(Lambda.class);
		Future<?> f = sut.submit(action, callback);
		Mockito.verify(action, Mockito.after(2).times(1)).action();
		Mockito.verify(callback, Mockito.times(0)).action();
		f.get();
		Mockito.verify(callback, Mockito.times(1)).action();

	}
}
