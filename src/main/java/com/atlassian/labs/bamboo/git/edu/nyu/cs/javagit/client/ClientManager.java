/*
 * ====================================================================
 * Copyright (c) 2008 JavaGit Project.  All rights reserved.
 *
 * This software is licensed using the GNU LGPL v2.1 license.  A copy
 * of the license is included with the distribution of this source
 * code in the LICENSE.txt file.  The text of the license can also
 * be obtained at:
 *
 *   http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 *
 * For more information on the JavaGit project, see:
 *
 *   http://www.javagit.com
 * ====================================================================
 */
package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.cli.CliClient;


/**
 * This class manages the <code>IClient</code> instances of the various <code>ClientType</code>s.
 * It contains factory type functionality (the <code>getClientType()</code> method) but manages
 * much more than just creating <code>IClient</code> instances.
 */
public class ClientManager {

  // An enumeration of the available client types.
  public static enum ClientType {
    CLI
  };

  // The singleton instance of the class <code>ClientManager</code>.
  private static ClientManager INSTANCE = new ClientManager();

  /**
   * Gets the singleton instance.
   * 
   * @return The singleton instance.
   */
  public static ClientManager getInstance() {
    return INSTANCE;
  }

  // The preferred client type.
  private ClientType preferredClientType = ClientType.CLI;

  /**
   * A <code>Map</code> to hold <code>IClient</code> instances for the ClientTypes by
   * <code>ClientType</code>.
   */
  private Map<ClientType, IClient> clientImpls = new HashMap<ClientType, IClient>();

  /**
   * Private to make this class a singleton.
   */
  private ClientManager() {
  }

  /**
   * Gets an instance of the specified client type.
   * 
   * @param clientType
   *          The type of client to get.
   * @return An instance of the specified client type.
   */
  public IClient getClientInstance(ClientType clientType) {
    IClient clientInstance = clientImpls.get(clientType);
    if (null == clientInstance) {
      if (ClientType.CLI == clientType) {
        clientInstance = new CliClient();
      }
      if (null != clientInstance) {
        clientImpls.put(clientType, clientInstance);
      }
    }
    return clientInstance;
  }

  /**
   * Gets an instance of the preferred client type as set via the
   * <code>setPreferredClientType()</code> method.
   * 
   * @return An IClient instance for the preferred client type.
   */
  public IClient getPreferredClient() {
    return getClientInstance(preferredClientType);
  }

  /**
   * Sets the preferred client type.
   * 
   * @param preferredClientType
   *          The type to set as the preferred client type. If the value is null, then the preferred
   *          client type is set to <code>ClientType.CLI</code>.
   */
  public void setPreferredClientType(ClientType preferredClientType) {
    if (null == preferredClientType) {
      this.preferredClientType = ClientType.CLI;
    } else {
      this.preferredClientType = preferredClientType;
    }
  }
}
