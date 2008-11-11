package at.rc.tacos.platform.net.handler;

import java.sql.SQLException;

import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.mina.ServerContext;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * This interface encapsulates all the handlers.
 * 
 * @author Michael
 */
public interface Handler<M> {

	/**
	 * Execute command.
	 * 
	 * @param session
	 *            The current {@link ServerIoSession}
	 * @param context
	 *            The current {@link ServerContext}
	 * @param request
	 *            The current {@link Message}
	 */
	void execute(ServerIoSession session, ServerContext context, Message<M> message) throws SQLException, ServiceException;

}
