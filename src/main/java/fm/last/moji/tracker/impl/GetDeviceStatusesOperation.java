package fm.last.moji.tracker.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.last.moji.tracker.TrackerException;

class GetDeviceStatusesOperation {

  private static final Logger log = LoggerFactory.getLogger(GetDeviceStatusesOperation.class);

  private final RequestHandler requestHandler;
  private final String domain;
  private Map<String, Map<String, String>> parametersByDevice;

  GetDeviceStatusesOperation(RequestHandler requestHandler, String domain) {
    this.requestHandler = requestHandler;
    this.domain = domain;
    parametersByDevice = Collections.emptyMap();
  }

  public void execute() throws TrackerException {
    Request request = new Request.Builder(3).command("get_devices").arg("domain", domain).build();
    Response response = requestHandler.performRequest(request);
    if (response.getStatus() != ResponseStatus.OK) {
      String message = response.getMessage();
      throw new TrackerException(message);
    }
    extractDeviceParameters(response);
  }

  Map<String, Map<String, String>> getParametersByDevice() {
    return Collections.unmodifiableMap(parametersByDevice);
  }

  private void extractDeviceParameters(Response response) {
    Map<String, String> valueMap = response.getValueMap();
    if (!valueMap.isEmpty()) {
      parametersByDevice = new HashMap<String, Map<String, String>>();
      for (Map.Entry<String, String> entry : valueMap.entrySet()) {
        String parameterName = entry.getKey();
        boolean parameterAdded = false;
        // ignoring the parameter of number of devices
        if (!"devices".equalsIgnoreCase(parameterName)) {
          int delimiterPosition = parameterName.indexOf('_');
          if (parameterName.length() > 2 && delimiterPosition >= 0) {
            String deviceName = parameterName.substring(0, delimiterPosition);
            parameterName = parameterName.substring(delimiterPosition + 1).toLowerCase();
            Map<String, String> parameters = parametersByDevice.get(deviceName);
            if (parameters == null) {
              parameters = new HashMap<String, String>();
              parametersByDevice.put(deviceName, parameters);
            }
            parameters.put(parameterName, entry.getValue());
            parameterAdded = true;
          }
        }
        if (!parameterAdded) {
          log.debug("Ignoring parameter named: " + parameterName);
        }
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("GetDeviceStatusesOperation [domain=");
    builder.append(domain);
    builder.append(", parametersByDevice=");
    builder.append(parametersByDevice);
    builder.append("]");
    return builder.toString();
  }

}
