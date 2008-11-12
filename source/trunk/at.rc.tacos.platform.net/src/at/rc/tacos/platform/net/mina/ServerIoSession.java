package at.rc.tacos.platform.net.mina;

import java.net.SocketAddress;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.core.session.TrafficMask;
import org.apache.mina.core.write.WriteRequest;

import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.message.MessageBuilder;
import at.rc.tacos.platform.services.Service;

/**
 * Wrapped {@linkplain IoSession} implementation to provides common functions. that are needed.
 * 
 * @author Michael
 */
public class ServerIoSession implements IoSession {

    // Attributes that are stored in the session
    public static final String ATTRIBUTE_PREFIX = "at.rc.tacos.server.";

    private static final String ATTRIBUTE_USER = ATTRIBUTE_PREFIX + "user";

    private static final String ATTRIBUTE_LOGIN_TIME = ATTRIBUTE_PREFIX + "login-time";

    private static final String ATTRIBUTE_FAILED_LOGINS = ATTRIBUTE_PREFIX + "failed-logins";

    // this is the wrapped session object
    private IoSession wrappedSession;

    /**
     * Default class constructor to create a new instance.
     * 
     * @param ioSession
     *            the session to wrapp
     */
    public ServerIoSession(IoSession ioSession) {
        this.wrappedSession = ioSession;
    }

    /**
     * Creates a new message based on the original {@link Message} and the result of the current
     * interaction with the {@link Handler} and the {@link Service} instances and brodcasts the and
     * sends this message to all connected an authenticated clients.
     * 
     * @param message
     *            the original received message from the client
     * 
     * @param objects
     *            the list of objects to send the message object to send
     */
    public void writeBrodcast(Message<? extends Object> originalMessage, Object... objects) {
        // setup a new message to send back to the client
        AbstractMessage<Object> message = MessageBuilder.buildMessage(originalMessage, objects);

        // loop over all sessions and send the reply
        for (Map.Entry<Long, IoSession> entry : wrappedSession.getService().getManagedSessions()
                .entrySet()) {
            ServerIoSession session = new ServerIoSession(entry.getValue());
            // assert that the session is autenticated
            if (!session.isLoggedIn()) {
                continue;
            }
            // send the message
            session.write(message);
        }
    }

    /**
     * Sets the session as authenticated and stores the user information.
     * 
     * @param username
     *            the username
     */
    public void setLoggedIn(String username) {
        setAttribute(ATTRIBUTE_LOGIN_TIME, Calendar.getInstance().getTimeInMillis());
        setAttribute(ATTRIBUTE_USER, username);
    }

    /**
     * Returns whether or not this session is authenticated.
     * 
     * @return true if the user is logged authenticated.
     */
    public boolean isLoggedIn() {
        return wrappedSession.containsAttribute(ATTRIBUTE_USER);
    }

    /**
     * Increments the failed login attemps for this session
     */
    public synchronized void increaseFailedLogins() {
        int failedLogins = (Integer) getAttribute(ATTRIBUTE_FAILED_LOGINS, 0);
        failedLogins++;
        setAttribute(ATTRIBUTE_FAILED_LOGINS, failedLogins);
    }

    /**
     * Returns the number of failed login attemps for the current session
     * 
     * @return the number of faild login tries.
     */
    public int getFailedLogins() {
        return (Integer) getAttribute(ATTRIBUTE_FAILED_LOGINS, 0);
    }

    /**
     * Returns the time in milliseconds when session was authenticated
     * 
     * @return the login time in milliseconds
     */
    public long getLoginTime() {
        return (Long) getAttribute(ATTRIBUTE_LOGIN_TIME);
    }

    /**
     * Resets the current state of the session and removes all attributes.
     */
    public void reinitialize() {
        removeAttribute(ATTRIBUTE_USER);
        removeAttribute(ATTRIBUTE_LOGIN_TIME);
    }

    /* Begin wrapped IoSession methods */

    /**
     * @see IoSession#close()
     */
    public CloseFuture close() {
        return wrappedSession.close();
    }

    /**
     * @see IoSession#close(boolean)
     */
    public CloseFuture close(boolean immediately) {
        return wrappedSession.close(immediately);
    }

    /**
     * @see IoSession#closeOnFlush()
     */
    public CloseFuture closeOnFlush() {
        return wrappedSession.closeOnFlush();
    }

    /**
     * @see IoSession#containsAttribute(Object)
     */
    public boolean containsAttribute(Object key) {
        return wrappedSession.containsAttribute(key);
    }

    /**
     * @see IoSession#getAttachment()
     */
    @SuppressWarnings("deprecation")
    public Object getAttachment() {
        return wrappedSession.getAttachment();
    }

    /**
     * @see IoSession#getAttribute(Object)
     */
    public Object getAttribute(Object key) {
        return wrappedSession.getAttribute(key);
    }

    /**
     * @see IoSession#getAttribute(Object, Object)
     */
    public Object getAttribute(Object key, Object defaultValue) {
        return wrappedSession.getAttribute(key, defaultValue);
    }

    /**
     * @see IoSession#getAttributeKeys()
     */
    public Set<Object> getAttributeKeys() {
        return wrappedSession.getAttributeKeys();
    }

    /**
     * @see IoSession#getBothIdleCount()
     */
    public int getBothIdleCount() {
        return wrappedSession.getBothIdleCount();
    }

    /**
     * @see IoSession#getCloseFuture()
     */
    public CloseFuture getCloseFuture() {
        return wrappedSession.getCloseFuture();
    }

    /**
     * @see IoSession#getConfig()
     */
    public IoSessionConfig getConfig() {
        return wrappedSession.getConfig();
    }

    /**
     * @see IoSession#getCreationTime()
     */
    public long getCreationTime() {
        return wrappedSession.getCreationTime();
    }

    /**
     * @see IoSession#getFilterChain()
     */
    public IoFilterChain getFilterChain() {
        return wrappedSession.getFilterChain();
    }

    /**
     * @see IoSession#getHandler()
     */
    public IoHandler getHandler() {
        return wrappedSession.getHandler();
    }

    /**
     * @see IoSession#getId()
     */
    public long getId() {
        return wrappedSession.getId();
    }

    /**
     * @see IoSession#getIdleCount(IdleStatus)
     */
    public int getIdleCount(IdleStatus status) {
        return wrappedSession.getIdleCount(status);
    }

    /**
     * @see IoSession#getLastBothIdleTime()
     */
    public long getLastBothIdleTime() {
        return wrappedSession.getLastBothIdleTime();
    }

    /**
     * @see IoSession#getLastIdleTime(IdleStatus)
     */
    public long getLastIdleTime(IdleStatus status) {
        return wrappedSession.getLastIdleTime(status);
    }

    /**
     * @see IoSession#getLastIoTime()
     */
    public long getLastIoTime() {
        return wrappedSession.getLastIoTime();
    }

    /**
     * @see IoSession#getLastReadTime()
     */
    public long getLastReadTime() {
        return wrappedSession.getLastReadTime();
    }

    /**
     * @see IoSession#getLastReaderIdleTime()
     */
    public long getLastReaderIdleTime() {
        return wrappedSession.getLastReaderIdleTime();
    }

    /**
     * @see IoSession#getLastWriteTime()
     */
    public long getLastWriteTime() {
        return wrappedSession.getLastWriteTime();
    }

    /**
     * @see IoSession#getLastWriterIdleTime()
     */
    public long getLastWriterIdleTime() {
        return wrappedSession.getLastWriterIdleTime();
    }

    /**
     * @see IoSession#getLocalAddress()
     */
    public SocketAddress getLocalAddress() {
        return wrappedSession.getLocalAddress();
    }

    /**
     * @see IoSession#getReadBytes()
     */
    public long getReadBytes() {
        return wrappedSession.getReadBytes();
    }

    /**
     * @see IoSession#getReadBytesThroughput()
     */
    public double getReadBytesThroughput() {
        return wrappedSession.getReadBytesThroughput();
    }

    /**
     * @see IoSession#getReadMessages()
     */
    public long getReadMessages() {
        return wrappedSession.getReadMessages();
    }

    /**
     * @see IoSession#getReadMessagesThroughput()
     */
    public double getReadMessagesThroughput() {
        return wrappedSession.getReadMessagesThroughput();
    }

    /**
     * @see IoSession#getReaderIdleCount()
     */
    public int getReaderIdleCount() {
        return wrappedSession.getReaderIdleCount();
    }

    /**
     * @see IoSession#getRemoteAddress()
     */
    public SocketAddress getRemoteAddress() {
        return wrappedSession.getRemoteAddress();
    }

    /**
     * @see IoSession#getScheduledWriteBytes()
     */
    public long getScheduledWriteBytes() {
        return wrappedSession.getScheduledWriteBytes();
    }

    /**
     * @see IoSession#getScheduledWriteMessages()
     */
    public int getScheduledWriteMessages() {
        return wrappedSession.getScheduledWriteMessages();
    }

    /**
     * @see IoSession#getService()
     */
    public IoService getService() {
        return wrappedSession.getService();
    }

    /**
     * @see IoSession#getServiceAddress()
     */
    public SocketAddress getServiceAddress() {
        return wrappedSession.getServiceAddress();
    }

    /**
     * @see IoSession#getTrafficMask()
     */
    public TrafficMask getTrafficMask() {
        return wrappedSession.getTrafficMask();
    }

    /**
     * @see IoSession#getTransportMetadata()
     */
    public TransportMetadata getTransportMetadata() {
        return wrappedSession.getTransportMetadata();
    }

    /**
     * @see IoSession#getWriterIdleCount()
     */
    public int getWriterIdleCount() {
        return wrappedSession.getWriterIdleCount();
    }

    /**
     * @see IoSession#getWrittenBytes()
     */
    public long getWrittenBytes() {
        return wrappedSession.getWrittenBytes();
    }

    /**
     * @see IoSession#getWrittenBytesThroughput()
     */
    public double getWrittenBytesThroughput() {
        return wrappedSession.getWrittenBytesThroughput();
    }

    /**
     * @see IoSession#getWrittenMessages()
     */
    public long getWrittenMessages() {
        return wrappedSession.getWrittenMessages();
    }

    /**
     * @see IoSession#getWrittenMessagesThroughput()
     */
    public double getWrittenMessagesThroughput() {
        return wrappedSession.getWrittenMessagesThroughput();
    }

    /**
     * @see IoSession#isClosing()
     */
    public boolean isClosing() {
        return wrappedSession.isClosing();
    }

    /**
     * @see IoSession#isConnected()
     */
    public boolean isConnected() {
        return wrappedSession.isConnected();
    }

    /**
     * @see IoSession#isIdle(IdleStatus)
     */
    public boolean isIdle(IdleStatus status) {
        return wrappedSession.isIdle(status);
    }

    /**
     * @see IoSession#read()
     */
    public ReadFuture read() {
        return wrappedSession.read();
    }

    /**
     * @see IoSession#removeAttribute(Object)
     */
    public Object removeAttribute(Object key) {
        return wrappedSession.removeAttribute(key);
    }

    /**
     * @see IoSession#removeAttribute(Object, Object)
     */
    public boolean removeAttribute(Object key, Object value) {
        return wrappedSession.removeAttribute(key, value);
    }

    /**
     * @see IoSession#replaceAttribute(Object, Object, Object)
     */
    public boolean replaceAttribute(Object key, Object oldValue, Object newValue) {
        return wrappedSession.replaceAttribute(key, oldValue, newValue);
    }

    /**
     * @see IoSession#resumeRead()
     */
    public void resumeRead() {
        wrappedSession.resumeRead();
    }

    /**
     * @see IoSession#resumeWrite()
     */
    public void resumeWrite() {
        wrappedSession.resumeWrite();
    }

    /**
     * @see IoSession#setAttachment(Object)
     */
    @SuppressWarnings("deprecation")
    public Object setAttachment(Object attachment) {
        return wrappedSession.setAttachment(attachment);
    }

    /**
     * @see IoSession#setAttribute(Object)
     */
    public Object setAttribute(Object key) {
        return wrappedSession.setAttribute(key);
    }

    /**
     * @see IoSession#setAttribute(Object, Object)
     */
    public Object setAttribute(Object key, Object value) {
        return wrappedSession.setAttribute(key, value);
    }

    /**
     * @see IoSession#setAttributeIfAbsent(Object)
     */
    public Object setAttributeIfAbsent(Object key) {
        return wrappedSession.setAttributeIfAbsent(key);
    }

    /**
     * @see IoSession#setAttributeIfAbsent(Object, Object)
     */
    public Object setAttributeIfAbsent(Object key, Object value) {
        return wrappedSession.setAttributeIfAbsent(key, value);
    }

    /**
     * @see IoSession#setTrafficMask(TrafficMask)
     */
    public void setTrafficMask(TrafficMask trafficMask) {
        wrappedSession.setTrafficMask(trafficMask);
    }

    /**
     * @see IoSession#suspendRead()
     */
    public void suspendRead() {
        wrappedSession.suspendRead();
    }

    /**
     * @see IoSession#suspendWrite()
     */
    public void suspendWrite() {
        wrappedSession.suspendWrite();
    }

    /**
     * @see IoSession#write(Object)
     */
    public WriteFuture write(Object message) {
        return wrappedSession.write(message);
    }

    /**
     * @see IoSession#write(Object, SocketAddress)
     */
    public WriteFuture write(Object message, SocketAddress destination) {
        return wrappedSession.write(message, destination);
    }

    /**
     * @see IoSession#getCurrentWriteMessage()
     */
    @Override
    public Object getCurrentWriteMessage() {
        return wrappedSession.getCurrentWriteMessage();
    }

    /**
     * @see IoSession#getCurrentWriteRequest()
     */
    @Override
    public WriteRequest getCurrentWriteRequest() {
        return wrappedSession.getCurrentWriteRequest();
    }

    /**
     * @see IoSession#isBothIdle()
     */
    @Override
    public boolean isBothIdle() {
        return wrappedSession.isBothIdle();
    }

    /**
     * @see IoSession#isReaderIdle()
     */
    @Override
    public boolean isReaderIdle() {
        return wrappedSession.isReaderIdle();
    }

    /**
     * @see IoSession#isWriterIdle()
     */
    @Override
    public boolean isWriterIdle() {
        return wrappedSession.isWriterIdle();
    }

    /* End wrapped IoSession methods */
}
