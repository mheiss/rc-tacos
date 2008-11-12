package at.rc.tacos.platform.net.handler;

import java.sql.SQLException;

import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * This interface encapsulates all the handlers.
 * 
 * @author Michael
 */
public interface Handler<M> {

    /**
     * Invoked to execute a {@linkplain MessageType#ADD} request.
     * 
     * @param session
     *            The current {@link ServerIoSession} accociated with the request
     * @param request
     *            The current {@link Message} request to process
     */
    public void add(ServerIoSession session, Message<M> message) throws SQLException,
            ServiceException;

    /**
     * Invoked to execute a {@linkplain MessageType#UPDATE} request.
     * 
     * @param session
     *            The current {@link ServerIoSession} accociated with the request
     * @param request
     *            The current {@link Message} request to process
     */
    public void update(ServerIoSession session, Message<M> message) throws SQLException,
            ServiceException;

    /**
     * Invoked to execute a {@linkplain MessageType#REMOVE} request.
     * 
     * @param session
     *            The current {@link ServerIoSession} accociated with the request
     * @param request
     *            The current {@link Message} request to process
     */
    public void remove(ServerIoSession session, Message<M> message) throws SQLException,
            ServiceException;

    /**
     * Invoked to execute a {@linkplain MessageType#GET} request.
     * 
     * @param session
     *            The current {@link ServerIoSession} accociated with the request
     * @param request
     *            The current {@link Message} request to process
     */
    public void get(ServerIoSession session, Message<M> message) throws SQLException,
            ServiceException;

    /**
     * Invoked to execute a {@linkplain MessageType#EXEC} request. This is a more general solution if
     * the {@linkplain MessageType#ADD}, {@linkplain MessageType#UPDATE}, {@linkplain MessageType#REMOVE} or
     * {@linkplain MessageType#GET} messages are not appropriate for the request
     * 
     * @param session
     *            The current {@link ServerIoSession} accociated with the request
     * @param request
     *            The current {@link Message} request to process
     */
    public void execute(ServerIoSession session, Message<M> message) throws SQLException,
            ServiceException;

}
