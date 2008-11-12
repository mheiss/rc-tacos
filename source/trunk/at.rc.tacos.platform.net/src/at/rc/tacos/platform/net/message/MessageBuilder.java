package at.rc.tacos.platform.net.message;

import java.util.Arrays;

import at.rc.tacos.platform.net.Message;

/**
 * Helper class to build message instances based on the original message
 * 
 * @author mheiss
 * 
 */
public class MessageBuilder {

    /**
     * Build a new messages instances based on the original message that received
     * 
     * @param originalMessage
     *            the message that was received
     * @param objects
     *            the objects that are the result from the current operations
     * @return the new message instance to send back to the client
     */
    public final static AbstractMessage<Object> buildMessage(Message<? extends Object> originalMessage,
            Object... objects) {
        // determine the type of message
        switch (originalMessage.getMessageType()) {
        case ADD:
            AddMessage<Object> addMessage = new AddMessage<Object>(Arrays.asList(objects));
            addMessage.setId(originalMessage.getId());
            return addMessage;
        case UPDATE:
            UpdateMessage<Object> updateMessage = new UpdateMessage<Object>(Arrays.asList(objects));
            updateMessage.setId(originalMessage.getId());
            return updateMessage;
        case REMOVE:
            RemoveMessage<Object> removeMessage = new RemoveMessage<Object>(Arrays.asList(objects));
            removeMessage.setId(originalMessage.getId());
            return removeMessage;
        case GET:
            GetMessage<Object> getMessage = new GetMessage<Object>();
            getMessage.setId(originalMessage.getId());
            getMessage.setObjects(Arrays.asList(objects));
            return getMessage;
        case EXEC:
            String messageCommand = originalMessage.getParams().get(
                    AbstractMessage.ATTRIBUTE_COMMAND);
            ExecMessage<Object> execMessage = new ExecMessage<Object>(messageCommand);
            execMessage.setId(originalMessage.getId());
            execMessage.setObjects(Arrays.asList(objects));
        }
        // nothing matched
        return null;
    }

}
