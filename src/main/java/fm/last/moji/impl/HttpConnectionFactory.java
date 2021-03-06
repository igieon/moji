/*
 * Copyright 2012 Last.fm
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package fm.last.moji.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

class HttpConnectionFactory {

  private final NetworkingConfiguration netConfig;

  @Deprecated
  HttpConnectionFactory(Proxy proxy) {
    netConfig = new NetworkingConfiguration.Builder().proxy(proxy).build();
  }

  HttpConnectionFactory(NetworkingConfiguration netConfig) {
    this.netConfig = netConfig;
  }

  HttpURLConnection newConnection(URL url) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) url.openConnection(netConfig.getProxy());
    connection.setConnectTimeout(netConfig.getHttpConnectTimeout());
    connection.setReadTimeout(netConfig.getHttpReadTimeout());
    return connection;
  }

}
