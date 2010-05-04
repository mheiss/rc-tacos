package at.rc.tacos.platform.net.message;

import java.util.List;

import at.rc.tacos.platform.model.SystemMessage;
import at.rc.tacos.platform.net.Message;

/**
 * Helper class to build message instances based on the original message
 * 
 * @author mheiss
 */
public class MessageBuilder {

	/**
	 * Build a new messages instances based on the original message that
	 * received
	 * 
	 * @param originalMessage
	 *            the message that was received
	 * @param objects
	 *            the objects that are the result from the current operations
	 * @return the new message instance to send back to the client
	 */
	@SuppressWarnings("unchecked")
	public final static AbstractMessage<Object> buildMessage(Message<? extends Object> originalMessage, List<? extends Object> objects) {
		// determine the type of message
		switch (originalMessage.getMessageType()) {
			case ADD:
				AddMessage<Object> addMessage = new AddMessage<Object>((List<Object>) objects);
				addMessage.setId(originalMessage.getId());
				return addMessage;
			case UPDATE:
				UpdateMessage<Object> updateMessage = new UpdateMessage<Object>((List<Object>) objects);
				updateMessage.setId(originalMessage.getId());
				return updateMessage;
			case REMOVE:
				RemoveMessage<Object> removeMessage = new RemoveMessage<Object>((List<Object>) objects);
				removeMessage.setId(originalMessage.getId());
				return removeMessage;
			case GET:
				GetMessage<Object> getMessage = new GetMessage<Object>();
				getMessage.setId(originalMessage.getId());
				getMessage.setObjects((List<Object>) objects);
				return getMessage;
			case EXEC:
				String messageCommand = originalMessage.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
				ExecMessage<Object> execMessage = new ExecMessage<Object>(messageCommand);
				execMessage.setId(originalMessage.getId());
				execMessage.setObjects((List<Object>) objects);
				return execMessage;
		}
		// nothing matched
		return null;
	}

	/**
	 * Builds a error message based on the original message. This is usefull
	 * when the {@link AbstractMessage#id} that identifies a request should be
	 * the same when sending back to the client.
	 * <p>
	 * The
	 * {@link AbstractMessage#synchronRequest(org.apache.mina.core.session.IoSession)}
	 * for example relies on this id to wait for the response.
	 * </p>
	 * 
	 * @param originalMessage
	 *            the message that was received
	 * @param errorMessages
	 *            the error message to set up the response
	 * @return the new message instance to send back to the client
	 */
	public final static AbstractMessage<Object> buildErrorMessage(Message<? extends Object> originalMessage, String... errorMessages) {

		// create a new error message
		ExecMessage<Object> execMessage = new ExecMessage<Object>("system.error");
		execMessage.setId(originalMessage.getId());

		// add the errors that occured
		for (String str : errorMessages) {
			SystemMessage systemMessage = new SystemMessage(str);
			execMessage.addObject(systemMessage);
		}

		return execMessage;
	}

}
