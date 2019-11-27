package Utility;

/**
 * Interface to be implemented by all POJO classes containing API authorization secrets.
 */
public interface ISecrets
{
    String getClientID();
    String getClientSecret();
    String getRequestKey();
    String getRequestKeyValue();
}
