// ISecurityCenter.aidl
package xyd.com.xiayuandongtest;

// Declare any non-default types here with import statements

interface ISecurityCenter {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String encrypt(String content);
    String decypt(String password);
}
