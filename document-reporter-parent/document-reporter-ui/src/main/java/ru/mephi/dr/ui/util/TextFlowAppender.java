package ru.mephi.dr.ui.util;

import org.apache.log4j.Level;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class TextFlowAppender extends WriterAppender {
	private static volatile TextFlow textFlow = null;

	/**
	 * Set the target TextFlow for the logging information to appear.
	 *
	 * @param textFlow
	 */
	public static void setTextFlow(final TextFlow textFlow) {
		if (textFlow == null) {
			throw new NullPointerException();
		}
		TextFlowAppender.textFlow = textFlow;
	}

	/**
	 * Format and then append the loggingEvent to the stored TextFlow.
	 *
	 * @param loggingEvent
	 */
	@Override
	public void append(final LoggingEvent loggingEvent) {
		final String message = this.layout.format(loggingEvent);
		// Append formatted message to text area using the Thread.
		try {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						if (textFlow != null) {
							Text t = new Text(message);
							if (loggingEvent.getLevel().equals(Level.ERROR)) {
								t.setFill(Color.RED);
							} else if (loggingEvent.getLevel().equals(Level.WARN)) {
								t.setFill(Color.ORANGE);
							}
							textFlow.getChildren().add(t);
						}
					} catch (final Throwable t) {
						System.out.println("Unable to append log to text area: " + t.getMessage());
						t.printStackTrace();
					}
				}
			});
		} catch (final IllegalStateException e) {
			// ignore case when the platform hasn't yet been iniitialized
		}
	}
}
